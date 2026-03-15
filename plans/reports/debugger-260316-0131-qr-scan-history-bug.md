# Debugger Report: QR Scan History Not Showing

Date: 2026-03-16
Scope: QR scan history feature - TraceabilityController, DAOQRScanHistory, result.jsp, history.jsp

---

## Summary

There are **3 confirmed bugs** and **1 critical architectural flaw** that together explain why scan history never appears after a user scans a QR code.

---

## Bug 1 (CRITICAL): Scan is Never Recorded Automatically on QR Lookup

**Files:** `TraceabilityController.java` lines 93-114, 116-166
**Root cause:** The scan record is only saved when the user manually submits the "Ghi nhan luot quet moi" form in `result.jsp` (POST action=`recordScan`). There is no automatic save on lookup or result display.

**Flow trace:**
1. User scans QR → GET `/traceability?qr=...` or POST `action=lookup`
2. `handleLookup` (line 93) only resolves the QR, then does `sendRedirect` to `action=result` — **no insert**
3. `showScanPage` (line 72) when `?qr=` is in URL also just redirects — **no insert**
4. `handleResult` (line 116) only reads data, does **not call** `dao.insert()`
5. Only `handleRecordScan` (line 168) ever calls `dao.insert()`, and that requires the user to fill and submit the form manually

**Impact:** Every real-world QR scan (phone camera scan → URL redirect) produces zero history records.

---

## Bug 2 (CRITICAL): Connection Closed After First DAO Query in `handleResult`

**Files:** `DAOQRScanHistory.java` line 49, `DAOBatchQRCode.java` line 49, `DBContext.java` line 17
**Root cause:** `closeResources(null, ps, rs)` is called in every `finally` block. The first argument `null` means the `Connection conn` is NOT closed here — that part is correct in isolation. **However**, `conn` is a shared instance field on `DBContext` (line 17 of `DBContext.java`). The pattern `closeResources(null, ps, rs)` never closes `conn`, which appears safe, but all DAO instances share one connection per object lifetime.

In `handleResult` (controller lines 121-157), **four separate DAO objects** are instantiated back-to-back:
```
new DAOBatchQRCode()      → opens conn1
new DAOProductionBatch()  → opens conn2
new DAOFarmProduct()      → opens conn3
new DAOMember()           → opens conn4
new DAOQRScanHistory()    → opens conn5
```
Each `new DAOXxx()` calls `super()` which calls `DriverManager.getConnection(...)`. All five connections are opened but **none are explicitly closed** after `handleResult` finishes (only PS/RS are closed in finally blocks). This is a connection leak. Under load, MySQL's max_connections will be exhausted, causing `DAOQRScanHistory.insert()` to fail silently (the `catch(SQLException)` at line 103 just prints stack trace and returns `false`).

**Specific lines:** `DAOQRScanHistory.java` line 102-108 — `insert()` swallows the exception and returns `false` with no propagation to the user.

---

## Bug 3 (CONFIRMED): `handleRecordScan` Sets Request Attributes That Are Never Seen

**File:** `TraceabilityController.java` lines 193-200

```java
// Line 193-200
if (success) {
    request.setAttribute("success", "Da ghi nhan lich su quet");   // LINE 194
} else {
    request.setAttribute("error", "Ghi nhan lich su that bai");    // LINE 196
}
response.sendRedirect(...);   // LINE 200 — redirect DISCARDS request attributes
```

`request.setAttribute()` is called at lines 194 and 196, but then `response.sendRedirect()` is called at line 200. A redirect starts a brand-new request — all attributes set on the old request are discarded. The success/error messages set here **never reach the JSP**.

Additionally, after the redirect to `action=result`, the `handleResult` method re-reads `scanHistory` from DB. If the insert failed silently (Bug 2), the history list will still be empty and the user sees no feedback at all.

---

## Bug 4 (DISPLAY): `fmt:formatDate` on `java.sql.Timestamp` in JSP

**File:** `result.jsp` line 114, `history.jsp` line 57

```jsp
<fmt:formatDate value="${scan.scanTime}" pattern="dd/MM/yyyy HH:mm"/>
```

`fmt:formatDate` works with `java.util.Date`. `java.sql.Timestamp` extends `java.util.Date` so it technically works, but when `scan.scanTime` is `null` (the DB column has no default enforced at app level), this will render as an empty string silently rather than "N/A". Not a crash, but misleading UI.

---

## Secondary Observations

| Location | Line | Observation |
|---|---|---|
| `TraceabilityController.java` | 150 | `scanHistory` initialized to `null`, not `new ArrayList<>()`. If `getByQrId` throws and returns `null`, JSP `${not empty scanHistory}` will be false and show "no history" — correct fallback but masks errors |
| `DAOQRScanHistory.java` | 113 | `getPaginated` uses MySQL `LIMIT/OFFSET` syntax — incompatible if DB is ever switched to SQL Server/Oracle, but fine for current MySQL setup |
| `history.jsp` | 38-55 | QR value and batch code are resolved by nested `c:forEach` loops (O(n*m) in JSP). For large datasets this is very slow, but not a bug in the current MVP scale |
| `DAOQRScanHistory.java` | 94 | `INSERT` does not include `scan_time` column, relying on DB default. If the column has no `DEFAULT CURRENT_TIMESTAMP`, the scan time will be `null` |

---

## Root Cause Summary

The primary reason scan history never shows: **there is no automatic scan recording when a QR code is looked up**. The feature requires users to manually fill and submit a form. This is almost certainly unintentional — the expected behavior is that visiting `/traceability?qr=...` should auto-record a scan event.

The secondary reason data may fail to insert even when the form is submitted: **the connection leak from multiple DAO instantiations + silent exception swallowing** means failures are invisible.

---

## Recommended Fixes (Actionable)

### Fix 1 — Auto-record scan on result display
In `handleResult` (controller line 157, after `scanHistory = daoHistory.getByQrId(...)`), insert an automatic scan record before loading the page:

```java
// After line 157 in handleResult — auto-record the scan visit
QRScanHistory autoRecord = new QRScanHistory();
autoRecord.setQrId(qrCode.getId());
autoRecord.setScanLocation(request.getRemoteAddr()); // or null
autoRecord.setScanActor("Public"); // anonymous web scan
autoRecord.setNote(null);
daoHistory.insert(autoRecord);
// Then re-fetch updated history
scanHistory = daoHistory.getByQrId(qrCode.getId());
```

### Fix 2 — Replace `sendRedirect` with `forward` in `handleRecordScan`, or use flash/session for messages
Line 200: change `sendRedirect` to `request.getRequestDispatcher(...).forward(request, response)` — OR store the message in session before redirect:
```java
request.getSession().setAttribute("flashSuccess", "Da ghi nhan lich su quet");
response.sendRedirect(...);
```
Then read and clear from session in result.jsp.

### Fix 3 — Ensure `qr_scan_history.scan_time` has DB default
Add `DEFAULT CURRENT_TIMESTAMP` to the `scan_time` column if not already set. Verify with:
```sql
SHOW CREATE TABLE qr_scan_history;
```

### Fix 4 — Close DAO connections after use in controller
Each DAO instance should close its own connection. Either add a `close()` method on `DBContext` and call it after each DAO use, or use a connection pool (HikariCP/DBCP).

---

## Unresolved Questions

1. Is the auto-record of scan history intentional (every visit records) or manual-only (form submit)? The UX implies auto-record but code implies manual.
2. Does `qr_scan_history.scan_time` have a `DEFAULT CURRENT_TIMESTAMP` in the actual DB schema? Cannot verify without DB access.
3. Is there a phone/camera QR scan flow that posts to a different URL that might call `recordScan` directly?
