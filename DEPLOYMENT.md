# 🚀 FREE DEPLOYMENT GUIDE - Smart City Waste Optimization Dashboard

Deploy your application online **completely FREE** using Render.com!

## 📋 Prerequisites

- GitHub account (free)
- Render.com account (free, no credit card required)
- Your application code (you already have it!)

---

## 🎯 Quick Start (5 Steps)

### Step 1: Push Your Code to GitHub

1. **Initialize Git** (if not already done):
```bash
cd "C:\Users\kaush\Downloads\Infosys\Smart City Waste Optimization Dashboard"
git init
git add .
git commit -m "Initial commit - Smart City Waste Optimization Dashboard"
```

2. **Create GitHub Repository**:
   - Go to https://github.com/new
   - Name: `smart-city-waste-optimization`
   - Keep it **Public** (required for free tier)
   - Don't initialize with README
   - Click **Create repository**

3. **Push to GitHub**:
```bash
git remote add origin https://github.com/YOUR_USERNAME/smart-city-waste-optimization.git
git branch -M main
git push -u origin main
```

### Step 2: Create Render Account

1. Go to https://render.com
2. Click **Get Started for Free**
3. Sign up with GitHub (recommended)
4. No credit card required! ✨

### Step 3: Deploy PostgreSQL Database

1. **In Render Dashboard**:
   - Click **New +** → **PostgreSQL**

2. **Configure Database**:
   - **Name**: `smart-waste-db`
   - **Database**: `smart_waste_db`
   - **User**: `smart_waste_user`
   - **Region**: Oregon (US West) - fastest free region
   - **PostgreSQL Version**: 15
   - **Plan**: **Free** 
   - Click **Create Database**

3. **Wait for deployment** (~2 minutes)

4. **Enable PostGIS Extension**:
   - Once database is ready, click **Connect** → **PSQL Command**
   - Copy the connection command
   - Open your terminal and paste it (press Enter)
   - Run this command:
   ```sql
   CREATE EXTENSION IF NOT EXISTS postgis;
   \dx postgis
   \q
   ```
   - You should see "postgis | 3.3.x" confirming it's installed

### Step 4: Deploy Spring Boot Application

1. **In Render Dashboard**:
   - Click **New +** → **Web Service**

2. **Connect Repository**:
   - Select **Build and deploy from a Git repository**
   - Click **Next**
   - Choose your GitHub repository: `smart-city-waste-optimization`
   - Click **Connect**

3. **Configure Web Service**:
   - **Name**: `smart-waste-optimization`
   - **Region**: Oregon (US West) - same as database
   - **Branch**: `main`
   - **Root Directory**: Leave empty
   - **Runtime**: Java
   - **Build Command**: 
   ```bash
   ./mvnw clean package -DskipTests
   ```
   - **Start Command**:
   ```bash
   java -Dserver.port=$PORT -jar target/waste-optimization-1.0.0.jar
   ```
   - **Plan**: **Free**
   - Click **Advanced** to add environment variables

4. **Add Environment Variables**:
   Click **Add Environment Variable** for each:
   
   | Key | Value |
   |-----|-------|
   | `DATABASE_URL` | Click **Add from Database** → Select `smart-waste-db` → `Internal Connection String` |
   | `DB_USERNAME` | Click **Add from Database** → Select `smart-waste-db` → `Username` |
   | `DB_PASSWORD` | Click **Add from Database** → Select `smart-waste-db` → `Password` |
   | `JAVA_OPTS` | `-Xmx512m -Xms256m` |

5. **Deploy**:
   - Click **Create Web Service**
   - Wait for build and deployment (~5-10 minutes)

### Step 5: Access Your Application

1. Once deployed, you'll get a URL like: `https://smart-waste-optimization.onrender.com`
2. Click the URL to open your dashboard!
3. Wait 30-60 seconds for first load (free tier spins down when inactive)

---

## 🎉 Your Application is LIVE!

### What You Get (For FREE):

✅ **Public URL** - Share with anyone  
✅ **PostgreSQL Database** - 256MB storage, 97 connection hours/month  
✅ **PostGIS Support** - Geospatial queries enabled  
✅ **Auto Deployments** - Push to GitHub = auto deploy  
✅ **SSL Certificate** - Free HTTPS  
✅ **750 Hours/Month** - Enough for 24/7 uptime  

### Important Notes:

⚠️ **Free Tier Limitations**:
- Database: 256MB storage (good for ~2,000 bins)
- Web service: Spins down after 15 minutes of inactivity
- First request after spin-down takes 50 seconds
- 97 database connection hours/month (use connection pooling)

⚠️ **Service Spin-Down**:
- Free services sleep after 15 min of inactivity
- First visit wakes it up (takes 30-60 seconds)
- Subsequent visits are instant!

---

## 🔄 How to Update Your Application

1. **Make code changes locally**
2. **Commit and push**:
```bash
git add .
git commit -m "Your update description"
git push origin main
```
3. **Auto-deployment** - Render automatically rebuilds and redeploys!
4. **Monitor** - Check build logs in Render dashboard

---

## 🐛 Troubleshooting

### Build Failed?

**Check Maven Wrapper Permissions**:
```bash
git update-index --chmod=+x mvnw
git commit -m "Fix mvnw permissions"
git push
```

### Database Connection Error?

1. Verify PostGIS is installed:
   - Go to Render Dashboard → Your database
   - Click **Connect** → **PSQL Command**
   - Run: `\dx postgis`

2. Check environment variables:
   - Go to your web service → **Environment**
   - Verify `DATABASE_URL`, `DB_USERNAME`, `DB_PASSWORD` are set

### Application Not Loading?

1. **Check Logs**:
   - Render Dashboard → Your web service → **Logs**
   - Look for startup errors

2. **First Load Delay**:
   - Wait 60 seconds on first visit (service waking up)
   - Refresh the page

### No Data on Map?

- The `DataInitializer` creates 50 sample bins on startup
- Check logs for "Created 50 sample waste bins"
- Try clicking "Refresh Data" button on dashboard

---

## 📊 Monitor Your Application

### Render Dashboard Shows:

1. **Metrics** - CPU, memory usage
2. **Logs** - Real-time application logs
3. **Events** - Deployment history
4. **Database** - Connection info, backups

### Application Features:

- 📍 **50 Sample Bins** - Auto-created on first startup
- 🔄 **Auto Updates** - Bins update every 30 seconds
- 🚛 **Route Optimization** - Click "Generate Optimized Route"
- 📊 **Live Stats** - Total bins, full bins, average fill level
- 🗺️ **Interactive Map** - Leaflet.js with real-time data

---

## 🎨 Customization Tips

### Change Depot Location:
Edit `application.properties`:
```properties
app.depot.latitude=YOUR_LATITUDE
app.depot.longitude=YOUR_LONGITUDE
```

### Adjust Update Interval:
Change simulation speed (milliseconds):
```properties
app.bin.simulation.interval=60000  # 1 minute
```

### Add More Bins:
Edit `DataInitializer.java` - Change line:
```java
for (int i = 1; i <= 100; i++) {  // Create 100 bins instead of 50
```

---

## 💡 Alternative Free Hosting Options

### Option 2: Railway.app
- Similar to Render
- 500 hours/month free
- $5 credit monthly
- Setup: Similar process, use Railway CLI

### Option 3: Fly.io
- 3 GB storage free
- More complex setup
- Better for high traffic
- Requires Dockerfile

### Option 4: Heroku Alternatives
- Heroku removed free tier in 2022
- Consider Render (recommended) or Railway

---

## 🔒 Security (For Production)

If deploying for real use:

1. **Change default passwords**
2. **Add Spring Security** for authentication
3. **Enable CORS** properly
4. **Use environment variables** for all secrets
5. **Enable database backups** (Render free tier has limited backups)

---

## 📞 Support Resources

- **Render Docs**: https://render.com/docs
- **Render Community**: https://community.render.com
- **Spring Boot Docs**: https://spring.io/projects/spring-boot
- **PostGIS Docs**: https://postgis.net/documentation

---

## ✅ Deployment Checklist

- [ ] Code pushed to GitHub (public repository)
- [ ] Render account created (no credit card needed)
- [ ] PostgreSQL database deployed on Render
- [ ] PostGIS extension enabled
- [ ] Web service deployed and connected to database
- [ ] Environment variables configured
- [ ] Application accessible via public URL
- [ ] Sample bins created automatically
- [ ] Route optimization working
- [ ] Map displaying correctly

---

## 🌟 You're Done!

Your Smart City Waste Optimization Dashboard is now **live and accessible worldwide**! 🎉

Share your URL: `https://your-app-name.onrender.com`

**Pro Tip**: Keep your application active by visiting it regularly, or use a free service like [UptimeRobot](https://uptimerobot.com) to ping your URL every 5 minutes (prevents spin-down).

---

## 📈 Next Steps

1. **Share your dashboard** with friends/colleagues
2. **Add custom data** - Replace sample bins with real locations
3. **Enhance features** - Add user authentication, historical data, reports
4. **Monitor usage** - Check Render dashboard for metrics
5. **Scale up** - Upgrade to paid tier if you need more resources

**Happy Deploying! 🚀**
