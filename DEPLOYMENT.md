# 🚀 Deployment Guide

This guide will help you deploy your WhatsApp Messenger Backend to free online platforms.

## 📋 Prerequisites

1. **GitHub Account** - Your code should be in a GitHub repository
2. **Railway Account** - Sign up at [railway.app](https://railway.app)
3. **PostgreSQL Database** - We'll use Railway's PostgreSQL service

## 🎯 Option 1: Railway Deployment (Recommended)

### Step 1: Prepare Your Repository

Your repository should now have these files:
- `railway.json` - Railway configuration
- `nixpacks.toml` - Build configuration
- `Procfile` - Process definition
- `Dockerfile` - Container configuration (✅ Fixed)
- `DatabaseConfig.java` - Railway database configuration (✅ Added)
- Updated `application.properties` - Environment variables

### Step 2: Deploy to Railway

1. **Sign up at Railway**: Go to [railway.app](https://railway.app) and create an account

2. **Connect GitHub**:
   - Click "New Project"
   - Select "Deploy from GitHub repo"
   - Choose your WhatsApp backend repository

3. **Add PostgreSQL Database**:
   - In your Railway project, click "New"
   - Select "Database" → "PostgreSQL"
   - Railway will automatically provide connection details

4. **Configure Environment Variables**:
   - Go to your app's "Variables" tab
   - Add these variables:
   ```
   JWT_SECRET=your-super-secure-jwt-secret-key-here
   PORT=8090
   ```
   - **Note**: Railway automatically provides `DATABASE_URL` when you add PostgreSQL

5. **Deploy**:
   - Railway will automatically build and deploy your app
   - The build process takes 2-3 minutes

### Step 3: Access Your App

- **API Base URL**: `https://your-app-name.railway.app/api`
- **Swagger UI**: `https://your-app-name.railway.app/api/swagger-ui.html`
- **Health Check**: `https://your-app-name.railway.app/api/actuator/health`

### Step 4: Test Your Deployment

Use the provided test script:
```bash
./test-deployment.sh https://your-app-name.railway.app
```

## 🎯 Option 2: Render Deployment

### Step 1: Sign up at Render
Go to [render.com](https://render.com) and create an account

### Step 2: Deploy
1. Click "New" → "Web Service"
2. Connect your GitHub repository
3. Configure:
   - **Name**: `whatsapp-backend`
   - **Environment**: `Java`
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/whatsapp-messenger-backend-1.0.0.jar`

### Step 3: Add PostgreSQL
1. Click "New" → "PostgreSQL"
2. Configure the database
3. Add environment variables in your web service

## 🎯 Option 3: Fly.io Deployment

### Step 1: Install Fly CLI
```bash
curl -L https://fly.io/install.sh | sh
```

### Step 2: Login and Deploy
```bash
fly auth login
fly launch
fly deploy
```

## 🔧 Environment Variables

For all platforms, set these environment variables:

```bash
# Database (Railway provides DATABASE_URL automatically)
DATABASE_URL=postgresql://username:password@host:port/database

# JWT
JWT_SECRET=your-super-secure-jwt-secret-key-here

# Server
PORT=8090

# Optional: Kafka (for paid tiers)
KAFKA_BOOTSTRAP_SERVERS=your-kafka-host:9092
```

## 🧪 Testing Your Deployment

### Automated Testing
Use the provided test script:
```bash
./test-deployment.sh https://your-app-url
```

### Manual Testing

#### 1. Health Check
```bash
curl https://your-app-url/api/actuator/health
```

#### 2. Register a User
```bash
curl -X POST https://your-app-url/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "TestPass123",
    "email": "test@example.com",
    "fullName": "Test User"
  }'
```

#### 3. Login
```bash
curl -X POST https://your-app-url/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "TestPass123"
  }'
```

#### 4. Access Swagger UI
Open: `https://your-app-url/api/swagger-ui.html`

## 🚨 Important Notes

### Free Tier Limitations:
1. **Railway**: 
   - 500 hours/month free
   - 1GB RAM, 1 CPU
   - 1GB storage

2. **Render**: 
   - 750 hours/month free
   - 512MB RAM
   - Spins down after 15 minutes of inactivity

3. **Fly.io**: 
   - 3 shared-cpu-1x 256mb VMs
   - 3GB persistent volume storage
   - 160GB outbound data transfer

### Kafka Limitation:
- **Free tiers don't support Kafka**
- The app will work without Kafka (event publishing will be disabled)
- For production, consider paid tiers or external Kafka services

### Database:
- All platforms provide PostgreSQL
- Railway and Render auto-provision databases
- Fly.io requires manual database setup

## 🔍 Troubleshooting

### Common Issues:

1. **Build Failures**:
   - Check Java version (requires Java 17)
   - Ensure all dependencies are in `pom.xml`
   - ✅ **Fixed**: Dockerfile no longer requires Maven wrapper files

2. **Database Connection**:
   - ✅ **Fixed**: Added `DatabaseConfig.java` to handle Railway's DATABASE_URL
   - Verify `DATABASE_URL` format
   - Check database credentials

3. **Port Issues**:
   - Ensure `PORT` environment variable is set
   - Railway uses `PORT` automatically

4. **Memory Issues**:
   - Free tiers have limited RAM
   - Consider reducing JVM heap size

### Debug Commands:
```bash
# Check logs
railway logs

# Check environment variables
railway variables

# Restart deployment
railway service restart
```

### Railway-Specific Issues:

#### Docker Build Failures
If you see errors like:
```
✕ [ 3/10] COPY mvnw . 
failed to calculate checksum of ref... "/mvnw": not found
```

**Solution**: ✅ **Fixed** - The Dockerfile has been updated to work without Maven wrapper files.

#### Database Connection Issues
If you see errors like:
```
Connection to localhost:5432 refused. Check that the hostname and port are correct
```

**Solution**: ✅ **Fixed** - Added `DatabaseConfig.java` to properly parse Railway's `DATABASE_URL` environment variable.

#### Build Timeout
If the build takes too long:
- Check if all dependencies are properly specified in `pom.xml`
- Ensure the repository is not too large
- Consider using `.dockerignore` to exclude unnecessary files

## 📊 Monitoring

### Railway:
- Built-in metrics dashboard
- Log streaming
- Automatic restarts

### Render:
- Log streaming
- Performance metrics
- Automatic scaling

### Fly.io:
- Built-in monitoring
- Log aggregation
- Performance insights

## 🔒 Security Considerations

1. **JWT Secret**: Use a strong, random secret
2. **Database**: Use strong passwords
3. **HTTPS**: All platforms provide SSL certificates
4. **Environment Variables**: Never commit secrets to Git

## 📈 Scaling

When you need to scale beyond free tiers:

1. **Railway**: Upgrade to paid plan
2. **Render**: Upgrade to paid plan
3. **Fly.io**: Pay for additional resources

## 🎉 Success!

Once deployed, your WhatsApp Messenger Backend will be accessible at:
- **API**: `https://your-app-url/api`
- **Documentation**: `https://your-app-url/api/swagger-ui.html`
- **Health**: `https://your-app-url/api/actuator/health`

Your backend is now ready for production use! 🚀 