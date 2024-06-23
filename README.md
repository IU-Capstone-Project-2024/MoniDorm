## Guide for .env

### BOT_TOKEN

Register a new telegram bot at [@BotFather](https://t.me/BotFather) and paste API Key Token

### EMAIL_ADDRESS

Use a your google gmail account

### EMAIL_PASSWORD

A regular password will not work. You need to registrate an app password. Follow this link https://myaccount.google.com/apppasswords

Registrate a new 16 symbol password and paste here

### EMAIL_SMTP_HOST

For gmail SMTP host is `smtp.gmail.com` Paste it as it is

### EMAIL_CODE_EXPIRATION_MINS

Set any number of minutes. Recomended 5-10 mins

### BOT_STORAGE_MONGO_URI

Link to deployed MongoDB database. Default localhost is `mongodb://localhost:27017`

For Docker, use `mongodb://ROOT_USERNAME:ROOT_PASSWORD@mongo:27017/`

### API_TOKEN

API Token for server that processes reports. In our case this is token for Java server

### API_URL

URL for API server

## How to run in Docker?
```docker-compose up --build```