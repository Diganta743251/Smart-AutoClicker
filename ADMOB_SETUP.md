# AdMob Setup Guide for TapCraft Pro

## 📱 AdMob Integration Setup

This guide covers the complete setup of AdMob integration for TapCraft Pro, including app-ads.txt configuration.

### 🔧 Current Configuration

**Publisher ID**: `pub-4084721334097026`
**App Package**: `com.tapcraft.pro.automation`

### 📋 Setup Checklist

#### ✅ 1. AdMob Application Setup
- [ ] Create AdMob account at [admob.google.com](https://admob.google.com)
- [ ] Add your app to AdMob console
- [ ] Get your actual AdMob App ID (format: `ca-app-pub-4084721334097026~XXXXXXXXXX`)
- [ ] Update `local.properties` with your real App ID

#### ✅ 2. Ad Unit Configuration
- [ ] Create Interstitial ad unit in AdMob console
- [ ] Update `adsUnitId` in build configuration
- [ ] Configure test devices for development

#### ✅ 3. App-ads.txt Setup
- [x] **COMPLETED**: app-ads.txt file created with your publisher ID
- [ ] Upload app-ads.txt to your developer website root
- [ ] Verify domain matches exactly with Google Play listing
- [ ] Wait 24 hours for AdMob to crawl and verify

### 🌐 Website Setup Instructions

#### Step 1: Upload app-ads.txt
Upload the `app-ads.txt` file to the root of your developer website:
```
https://yourdomain.com/app-ads.txt
```

#### Step 2: Verify Domain
Ensure the domain in your app-ads.txt matches exactly with:
- Google Play Console developer profile
- App Store developer profile (if applicable)

#### Step 3: Content Verification
Your app-ads.txt should contain:
```
google.com, pub-4084721334097026, DIRECT, f08c47fec0942fa0
```

### 🔧 Local Development Configuration

#### Update local.properties
Replace the test App ID with your production App ID:
```properties
# Replace XXXXXXXXXX with your actual app ID suffix
adsApplicationId=ca-app-pub-4084721334097026~XXXXXXXXXX
```

#### Test Device Configuration
Add your test devices to avoid live ads during development:
```properties
# Add your device IDs (comma-separated)
adsTestDevicesIds=YOUR_DEVICE_ID_1,YOUR_DEVICE_ID_2
```

### 🚀 Build Variants

#### F-Droid Variant
- No AdMob integration (open-source version)
- No ads or monetization features

#### PlayStore Variant
- Full AdMob integration
- Interstitial ads on scenario completion
- GDPR compliance with User Messaging Platform

### 📊 AdMob Console Verification

After 24 hours, verify in AdMob console:
1. Go to **Apps** section
2. Select your app
3. Click **app-ads.txt** tab
4. Check status shows "✅ Verified"

### 🔒 Privacy & Compliance

#### GDPR Compliance
- User Messaging Platform integrated
- Consent collection before ad serving
- Privacy policy updated with ad data usage

#### App Store Policies
- Ads clearly marked as advertisements
- No misleading ad placements
- Compliance with platform ad policies

### 🐛 Troubleshooting

#### Common Issues
1. **App-ads.txt not found**: Verify file is at website root
2. **Domain mismatch**: Ensure domain matches Play Console exactly
3. **Publisher ID mismatch**: Verify pub-ID is correct
4. **24-hour delay**: AdMob needs time to crawl and verify

#### Debug Commands
```bash
# Test app-ads.txt accessibility
curl https://yourdomain.com/app-ads.txt

# Verify content
cat app-ads.txt
```

### 📞 Support Resources

- [AdMob Help Center](https://support.google.com/admob)
- [App-ads.txt Specification](https://iabtechlab.com/ads-txt/)
- [Google Play Policy Center](https://support.google.com/googleplay/android-developer/topic/9858052)

---

## 🎯 Next Steps

1. **Get your actual AdMob App ID** from AdMob console
2. **Update local.properties** with production App ID
3. **Upload app-ads.txt** to your website root
4. **Wait 24 hours** for verification
5. **Test ads** in staging environment
6. **Submit for review** to app stores

---

*Last updated: January 2025*