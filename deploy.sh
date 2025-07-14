#!/bin/bash

echo "🚀 WhatsApp Messenger Backend Deployment Helper"
echo "================================================"

# Check if git is initialized
if [ ! -d ".git" ]; then
    echo "❌ Git repository not found!"
    echo "Please initialize git and push to GitHub first:"
    echo "git init"
    echo "git add ."
    echo "git commit -m 'Initial commit'"
    echo "git remote add origin <your-github-repo-url>"
    echo "git push -u origin main"
    exit 1
fi

# Check if remote exists
if ! git remote get-url origin > /dev/null 2>&1; then
    echo "❌ No remote repository found!"
    echo "Please add your GitHub repository:"
    echo "git remote add origin <your-github-repo-url>"
    exit 1
fi

echo "✅ Git repository found"
echo ""

echo "📋 Deployment Options:"
echo "1. Railway (Recommended - Easiest)"
echo "2. Render"
echo "3. Fly.io"
echo ""

read -p "Choose deployment platform (1-3): " choice

case $choice in
    1)
        echo ""
        echo "🚂 Railway Deployment"
        echo "===================="
        echo "1. Go to https://railway.app"
        echo "2. Sign up/Login with GitHub"
        echo "3. Click 'New Project'"
        echo "4. Select 'Deploy from GitHub repo'"
        echo "5. Choose your repository"
        echo "6. Add PostgreSQL database:"
        echo "   - Click 'New' → 'Database' → 'PostgreSQL'"
        echo "7. Set environment variables:"
        echo "   - JWT_SECRET=your-super-secure-jwt-secret-key-here"
        echo "   - PORT=8090"
        echo "8. Wait for deployment (2-3 minutes)"
        echo ""
        echo "Your app will be available at: https://your-app-name.railway.app"
        ;;
    2)
        echo ""
        echo "🎨 Render Deployment"
        echo "==================="
        echo "1. Go to https://render.com"
        echo "2. Sign up/Login with GitHub"
        echo "3. Click 'New' → 'Web Service'"
        echo "4. Connect your GitHub repository"
        echo "5. Configure:"
        echo "   - Name: whatsapp-backend"
        echo "   - Environment: Java"
        echo "   - Build Command: mvn clean package -DskipTests"
        echo "   - Start Command: java -jar target/whatsapp-messenger-backend-1.0.0.jar"
        echo "6. Add PostgreSQL database"
        echo "7. Set environment variables"
        echo ""
        echo "Your app will be available at: https://your-app-name.onrender.com"
        ;;
    3)
        echo ""
        echo "✈️  Fly.io Deployment"
        echo "===================="
        echo "1. Install Fly CLI:"
        echo "   curl -L https://fly.io/install.sh | sh"
        echo "2. Login: fly auth login"
        echo "3. Deploy: fly launch"
        echo "4. Follow the prompts"
        echo "5. Set environment variables in Fly dashboard"
        echo ""
        echo "Your app will be available at: https://your-app-name.fly.dev"
        ;;
    *)
        echo "❌ Invalid choice. Please select 1, 2, or 3."
        exit 1
        ;;
esac

echo ""
echo "🔧 Environment Variables to Set:"
echo "================================"
echo "DATABASE_URL=jdbc:postgresql://your-db-host:5432/your-db-name"
echo "DB_USERNAME=your-username"
echo "DB_PASSWORD=your-password"
echo "JWT_SECRET=your-super-secure-jwt-secret-key-here"
echo "PORT=8090"
echo ""

echo "🧪 Test Your Deployment:"
echo "========================"
echo "1. Health Check:"
echo "   curl https://your-app-url/api/actuator/health"
echo ""
echo "2. Register User:"
echo "   curl -X POST https://your-app-url/api/auth/register \\"
echo "     -H 'Content-Type: application/json' \\"
echo "     -d '{\"username\":\"testuser\",\"password\":\"TestPass123\",\"email\":\"test@example.com\",\"fullName\":\"Test User\"}'"
echo ""
echo "3. Swagger UI:"
echo "   https://your-app-url/api/swagger-ui.html"
echo ""

echo "📚 For detailed instructions, see DEPLOYMENT.md"
echo "🎉 Happy deploying!" 