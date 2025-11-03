# Documentation — Member 3 (Zeini Ddin) — Vulnerabilities & Malware Integration

**Role:** Vulnerabilities & Malware Integration

**Scope covered:** Implementation of malware functionality (Mic Recording) and persistence setup.

-----

## Project Overview

The *catsadoption_shop* application is a simple Kotlin/Jetpack Compose project used to simulate a mobile pentesting environment. This module's focus is on integrating intentional vulnerabilities and malware components that can be later exploited and bypassed by other team members (especially Member 4).

[cite\_start]The initial feature implemented by this member demonstrates how a benign-sounding feature ("Cat Communicator") can be used as a cover to perform background data exfiltration (mic recording)[cite: 29, 35]. The implementation prioritizes persistence using Android's built-in background execution tools.

-----

## Summary

One major feature implementation was developed:

1.  [cite\_start]**Malware Integration – Persistent Audio Eavesdropping:** Implementation of stealthy, periodic audio recording using `WorkManager` and `MediaRecorder`[cite: 29]. The functionality is triggered by a benign-looking button in the UI.

[cite\_start]The functionality is confirmed to be working and generating audio files in the app's internal cache directory, ready for retrieval and demonstration by the exploitation team[cite: 33].

-----

## Deliverables and Purpose

### Feature 1 — Persistent Audio Eavesdropping

[cite\_start]**Objective:** Implement malware functionality (mic recording) that runs persistently in the background[cite: 29, 35].

**Main Files:**

* `PermissionCheckActivity.kt` – Handles the runtime request for `android.permission.RECORD_AUDIO`.
* `StealthAudioWorker.kt` – A `CoroutineWorker` responsible for background scheduling and calling the recording logic.
* `recordAudioForTime()` function – The core function using `MediaRecorder` to capture audio.
* [cite\_start]`CatListingsScreen.kt` – Contains the visible "Cat Communicator" button used as the benign cover[cite: 29].

**Flow:**

1.  User clicks the "Cat Communicator" button.
2.  `PermissionCheckActivity` requests `RECORD_AUDIO` permission.
3.  If granted, the activity schedules the `StealthAudioWorker` periodically using `WorkManager`.
4.  The Worker executes, calling `recordAudioForTime()` to record 15 seconds of audio.
5.  Audio is saved to the app's private cache directory.

**Result:**

* [cite\_start]Malware functionality implemented and functional[cite: 35].
* The worker is scheduled to run every **15 minutes** (the minimum interval for `PeriodicWorkRequestBuilder`), demonstrating persistence.
* [cite\_start]Generated audio files are ready for exfiltration documentation[cite: 33].

-----

## Technical Explanation – Stealth and Persistence

The key to the implementation is balancing persistence with stealth:

### A. Persistence (`WorkManager`)

* The `PeriodicWorkRequestBuilder` ensures the recording task (tagged `AUDIO_MALWARE_TAG`) repeats every 15 minutes, even if the application is closed.
* This approach uses Android's sanctioned background processing, making it robust against general operating system restrictions.

### B. Stealth (Resource Handling)

* The `MediaRecorder` functionality is wrapped in a `try/catch/finally` block to ensure `recorder.release()` is called, freeing the microphone resource immediately after recording. This prevents interference with other user apps (like voice assistants or phone calls), which might alert the user.
* Files are saved to the app's **internal cache**: `/data/data/com.example.catsadoption_shop/cache/`. This location is inaccessible without **root access**, making data retrieval for the attacker a necessary demonstration of privilege escalation.

-----

## Exploitation and Validation

To validate the malware's success and retrieve the "stolen" audio data, Member 4 must perform the following:

1.  **Grant Permission:** Ensure the `RECORD_AUDIO` permission is granted to initiate the work.
2.  **Wait for Execution:** Allow at least 15 minutes for the `StealthAudioWorker` to execute and generate a file.
3.  **Root Access:** Gain root access to the device (required to access the protected `/data/data/` directory).
4.  **Retrieve Files:** Use `adb pull` to retrieve the audio files (named `audio_record_[timestamp].3gp`) from the cache directory:

<!-- end list -->

```bash
adb shell su -c 'ls /data/data/com.example.catsadoption_shop/cache/'
adb pull /data/data/com.example.catsadoption_shop/cache/audio_record_123456789.3gp stolen_audio.3gp
```

-----

**End of documentation draft — Feature 1 (Malware Integration)**

-----

**Ready for the next step?** Let's implement the **hidden Admin Dashboard** using the `IS_ADMIN_ENABLED` flag.

**Please use this prompt in the Gemini AI plugin:**

> "In Kotlin, create a simple utility object named `AppConfig` in its own file (`AppConfig.kt`) with a hardcoded, publicly accessible `const val` Boolean property named `IS_ADMIN_ENABLED` and set its value to **`false`**."