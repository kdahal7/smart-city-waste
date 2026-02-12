# 🚀 QUICK DEPLOY - 5 Minutes to Live!

## Prerequisites
- GitHub account
- Render.com account (no credit card!)

## Step-by-Step

### 1️⃣ Push to GitHub (2 minutes)
```bash
cd "C:\Users\kaush\Downloads\Infosys\Smart City Waste Optimization Dashboard"
git init
git add .
git commit -m "Deploy Smart City Waste Dashboard"
git remote add origin https://github.com/YOUR_USERNAME/smart-city-waste.git
git push -u origin main
```

### 2️⃣ Create Render Account (1 minute)
- Visit https://render.com
- Sign up with GitHub
- No credit card required!

### 3️⃣ Deploy Database (2 minutes)
1. Render Dashboard → **New +** → **PostgreSQL**
2. Settings:
   - Name: `smart-waste-db`
   - Region: Oregon
   - Plan: **Free**
3. Click **Create Database**
4. Once ready, connect via PSQL and run:
   ```sql
   CREATE EXTENSION IF NOT EXISTS postgis;
   ```

### 4️⃣ Deploy Application (3 minutes)
1. Render Dashboard → **New +** → **Web Service**
2. Connect your GitHub repo
3. Settings:
   - Name: `smart-waste-optimization`
   - Runtime: Java
   - Build: `./mvnw clean package -DskipTests`
   - Start: `java -Dserver.port=$PORT -jar target/waste-optimization-1.0.0.jar`
   - Plan: **Free**
4. Environment Variables:
   - `DATABASE_URL` → Link to database
   - `DB_USERNAME` → Link to database
   - `DB_PASSWORD` → Link to database
5. Click **Create Web Service**

### 5️⃣ Access Your App! (Wait ~5-10 minutes for build)
Your URL: `https://smart-waste-optimization.onrender.com`

## ⚡ First Visit
- Takes 60 seconds (service waking up)
- After that, instant!
- 50 sample bins auto-created
- Route optimization ready to use

## 📖 Full Guide
See [DEPLOYMENT.md](DEPLOYMENT.md) for detailed instructions and troubleshooting.

---

**That's it! You're live! 🎉**
