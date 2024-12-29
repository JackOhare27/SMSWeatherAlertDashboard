
# 🌦️ Weather Alert Central

**Weather Alert Central** is a Java-based GUI application that provides real-time weather updates 🌐 and sends SMS alerts 📱 based on user-defined parameters. The application leverages OpenWeather API 🌩️ for weather data and Twilio API for SMS notifications.

## ✨ Features
- **🌍 Real-Time Weather Updates**: Displays current weather conditions including temperature 🌡️, humidity 💧, wind speed 🌬️, and precipitation ☔.
- **⚙️ Customizable Alert Parameters**: Set thresholds for wind speed, temperature, and precipitation to trigger alerts.
- **📩 SMS Notifications**: Automatically sends alerts to a list of phone numbers when weather conditions exceed set thresholds.
- **📋 Phone Number Management**: Add and remove phone numbers from the notification list directly through the application.
- **🔴 Toggle Alert System**: Enable or disable SMS notifications with a simple switch.

## 🚀 Getting Started

### ✅ Prerequisites
- **☕ Java Development Kit (JDK)**: Ensure JDK 8 or later is installed.
- **🔑 Twilio Account**: Sign up for a Twilio account to obtain your `ACCOUNT_SID` and `AUTH_TOKEN`.
- **🔒 OpenWeather API Key**: Register for an API key from [OpenWeather](https://openweathermap.org/api).

### 📥 Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/weather-alert-central.git
   cd weather-alert-central
   ```
2. Ensure the required libraries are included in your project:
   - 📦 Apache HTTPClient
   - 📦 Google Gson

3. Add your credentials:
   - Replace placeholders for `ACCOUNT_SID`, `AUTH_TOKEN`, and `API_KEY` in the source code with your respective credentials.

### 🛠️ Compilation and Execution
1. Compile the program:
   ```bash
   javac CSC406_JackOHare_0589474_FP.java
   ```
2. Run the application:
   ```bash
   java CSC406_JackOHare_0589474_FP
   ```

## 🧑‍💻 Usage
1. Enter the name of the city 🏙️ you want to monitor for weather updates.
2. Use the GUI to:
   - 📞 Add phone numbers to the notification list.
   - 📝 View current weather details.
   - ⚙️ Set alert thresholds for weather conditions.
   - 🔴 Toggle the SMS alert system on or off.
3. Alerts 🚨 are sent automatically when weather conditions exceed the set parameters.

## 📂 File Structure
- **`CSC406_JackOHare_0589474_FP.java`**: Main program file containing the application logic and GUI.
- **`phone_numbers.txt`**: Stores the list of phone numbers for notifications.

## 📜 Dependencies
- [Apache HTTPClient](https://hc.apache.org/) 🌐
- [Google Gson](https://github.com/google/gson) 🛠️
- [Twilio Java SDK](https://www.twilio.com/docs/libraries/java) 📲

## 📸 Screenshots
![Screenshot 2024-11-18 212110](https://github.com/user-attachments/assets/aac01b35-43fe-4eee-b09e-f4d3385b9576)

## ⚠️ Limitations
- ❌ The program does not include input validation for API keys or phone numbers during setup.
- ❌ Limited error handling for API responses and network failures.

## 📜 License
This project is licensed under the WTFPL License.

## 🙌 Acknowledgments
- 🌦️ OpenWeather API for weather data.
- 📱 Twilio API for SMS notifications.

## 🤝 Contributing
Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

---

*Created by Jack O'Hare
