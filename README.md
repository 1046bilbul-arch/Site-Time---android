# Site Time — Android

Offline field activity and time tracking for Android.

## Setup
Upload the **contents** of Site-Time-Android-Source.zip (the files inside its site-time folder) to the repository root. Preserve the .github/workflows/android.yml path.

## Time accounting
Each event starts an activity interval which ends at the next event. Reports clip intervals to the selected date range before calculating category subtotals. Events with no subsequent end event do not accrue hours. Use a Clock Off category/event to end a workday, and ensure non-working categories are excluded from billable totals.

## Privacy
Entries remain locally on the Android device. Export JSON backups regularly and keep them somewhere secure. Debug APKs are intended for testing, not public distribution.
