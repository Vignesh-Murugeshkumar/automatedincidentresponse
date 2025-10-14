# Twilio SMS Setup Guide

## 1. Create Twilio Account
- Go to https://www.twilio.com/
- Sign up for free account
- Get $15 free credit

## 2. Get Credentials
From Twilio Console:
- **Account SID**: Found on dashboard
- **Auth Token**: Found on dashboard  
- **Phone Number**: Get a Twilio phone number

## 3. Set Environment Variables

### Windows (PowerShell):
```powershell
$env:TWILIO_ACCOUNT_SID = "your_account_sid_here"
$env:TWILIO_AUTH_TOKEN = "your_auth_token_here"  
$env:TWILIO_PHONE_NUMBER = "+1234567890"
```

### Windows (Command Prompt):
```cmd
set TWILIO_ACCOUNT_SID=your_account_sid_here
set TWILIO_AUTH_TOKEN=your_auth_token_here
set TWILIO_PHONE_NUMBER=+1234567890
```

## 4. Test SMS
After setting environment variables, restart your application and:
- Visit: http://localhost:8080/api/test/trojan
- Create trojan file: `echo test > C:\temp\trojan_test.exe`

## 5. Without Twilio Setup
The application works in simulation mode if Twilio is not configured.
You'll see SMS logs in console instead of real SMS delivery.

## 6. Verify Phone Number
For free Twilio accounts, you need to verify the destination phone number (+919940194051) in Twilio console before sending SMS.