//Jack O'Hare 0589474 jmohare@cn.edu
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.util.Timer;

@SuppressWarnings("serial")
class GUIBasics extends JFrame {

    private DefaultListModel<String> phoneNumbersModel; // Model for the phone numbers list
    private JList<String> phoneNumbersList; // JList to display phone numbers

    private Timer timer; // Class-level variable for the Timer
    private TimerTask task; // Class-level variable for the TimerTask

    
    public static String city = null;
	public static double temp;
	public static double windSpeed;
	public static String condition;
	public static double precipitation;
	public static double feelsLike;
	public static int humidity;
	public static boolean isThunder = false;
	public static String iconUrl;
	
	 JLabel photoLabel;
	 JTextField conditionField; 
	 JTextField tempField;
	 JTextField humidityField;
	 JTextField windField;
	 JTextField precField;
	
	
	
	//limits
	public double windLimi = 0;
	public double tempMin = 0;
	public double tempMax = 0;
	public double rainLimit = 0;
    
    public GUIBasics(String city) {
    	
    	//City/Weather Related
    	this.city = city;
    	weatherInfo weather = new weatherInfo(city);
    	temp = weather.temp;
    	iconUrl = weather.iconUrl;
    	windSpeed = weather.windSpeed;
    	condition = weather.condition;
    	precipitation = weather.precipitation;
    	feelsLike = weather.feelsLike;
    	humidity = weather.humidity;
    	isThunder = weather.isThunder;
    	
    	
    	//GUI
        // Set title for the window
        setTitle("Weather Alert Central");

        // Set the size of the window
        setSize(1000, 1000);

        // Specify what happens when the close button is clicked
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the layout manager to a grid layout for better panel organization
        setLayout(new GridLayout(2, 2)); // 2 rows and 2 columns

        // Add components here
        initializeComponents();
    }

    private void initializeComponents() {
        for (int i = 1; i <= 4; i++) {
            JPanel panel = new JPanel(); // Create a panel
            panel.setLayout(new BorderLayout()); // Use BorderLayout for the main layout
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add a border for clarity

            if (i == 1) {
                // Panel 1: Phone Number Input Form and Display
                initializePhoneNumberForm(panel);
                JLabel bottomLabel = new JLabel("Phone Number Alert List ", SwingConstants.CENTER);
                panel.add(bottomLabel, BorderLayout.SOUTH); // Add text at the bottom
                
            } else if (i == 2) {
            	InitalizeWeather(panel);
            	JLabel bottomLabel = new JLabel("Today's Weather ", SwingConstants.CENTER);
                panel.add(bottomLabel, BorderLayout.SOUTH); // Add text at the bottom
                   
            }
            else if (i == 3) {
               IntializeWeatherParamters(panel);    	
            }
            else {
        	   JLabel bottomLabel = new JLabel("Turn On/Off Phone Alerts ", SwingConstants.CENTER);
               panel.add(bottomLabel, BorderLayout.SOUTH); // Add text at the bottom
               IntializerService(panel);
            }
            // Add the panel to the frame
            add(panel);
        }
    }


    
    //////////////////////////////////////////////////////////////////////
    ///////////////////////////Panel 1////////////////////////////////////
    //////////////////////////////////////////////////////////////////////  
    
    private void initializePhoneNumberForm(JPanel panel) {
        // Create a panel with vertical alignment for the input components
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS)); // Vertical stacking

        // Create a label and text field for phone number input
        JLabel inputLabel = new JLabel("Enter Phone Number:");
        JTextField phoneNumberField = new JTextField(15); // 15-character input field

        // Create buttons for saving and removing numbers
        JButton saveButton = new JButton("Add Phone Number");
        JButton removeButton = new JButton("Remove Selected Number");

        // Add the components to the input panel
        inputPanel.add(inputLabel);
        inputPanel.add(phoneNumberField);
        inputPanel.add(saveButton);
        inputPanel.add(removeButton);

        // Create a list model and JList to display the phone numbers
        phoneNumbersModel = new DefaultListModel<>();
        phoneNumbersList = new JList<>(phoneNumbersModel);
        phoneNumbersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Load existing numbers into the list model
        loadPhoneNumbers();

        // Add the JList to a scroll pane
        JScrollPane scrollPane = new JScrollPane(phoneNumbersList); // Add scrolling capability

        // Add the input panel and the scroll pane to the main panel
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Save number/validation
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String phoneNumber = phoneNumberField.getText();
                if (!phoneNumber.isEmpty() && phoneNumber.matches("\\d{10}")) { // Simple validation for 10 digits
                    savePhoneNumberToFile(phoneNumber);
                    phoneNumbersModel.addElement(phoneNumber); // Add to list model
                    JOptionPane.showMessageDialog(panel, "Phone number saved!");
                    phoneNumberField.setText(""); // Clear the input field
                } else {
                    JOptionPane.showMessageDialog(panel, "Please enter a valid 10-digit phone number.");
                }
            }
        });

        // Add action listener to the remove button
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedNumber = phoneNumbersList.getSelectedValue();
                if (selectedNumber != null) {
                    phoneNumbersModel.removeElement(selectedNumber); // Remove from list model
                    removePhoneNumberFromFile(selectedNumber);
                    JOptionPane.showMessageDialog(panel, "Phone number removed.");
                } else {
                    JOptionPane.showMessageDialog(panel, "Please select a phone number to remove.");
                }
            }
        });
    }


    private void savePhoneNumberToFile(String phoneNumber) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("phone_numbers.txt", true))) {
            writer.write(phoneNumber);
            writer.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadPhoneNumbers() {
        try (BufferedReader reader = new BufferedReader(new FileReader("phone_numbers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                phoneNumbersModel.addElement(line); // Add each number to the list model
            }
        } catch (IOException ex) {
            // If the file doesn't exist, start with an empty list
            ex.printStackTrace();
        }
    }

    private void removePhoneNumberFromFile(String phoneNumberToRemove) {
        List<String> numbers = new ArrayList<>();
        
        // Read all phone numbers except the one to remove
        try (BufferedReader reader = new BufferedReader(new FileReader("phone_numbers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.equals(phoneNumberToRemove)) {
                    numbers.add(line); // Keep other numbers
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        // Write back all remaining numbers to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("phone_numbers.txt"))) {
            for (String number : numbers) {
                writer.write(number);
                writer.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    //////////////////////////////////////////////////////////////////////
    ///////////////////////////Panel 2////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    
    private void InitalizeWeather(JPanel panel) {
        // Panel 2: Add a photo with labels and text fields using GridBagLayout
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for the inner panel
        GridBagConstraints gbc = new GridBagConstraints();

        // Add a larger label above the photo
        JLabel titleLabel = new JLabel(this.city, SwingConstants.CENTER); // Larger text above the image
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Set a larger font size (18 is just an example)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Make the title span across 2 columns
        innerPanel.add(titleLabel, gbc);

        // Create a JLabel for the photo
        photoLabel = new JLabel();
        try {
            // Construct the icon URL (assuming it's already provided in the correct format)
            URL url = new URL(iconUrl);  // Ensure iconUrl is a valid URL string
            ImageIcon originalIcon = new ImageIcon(url); // Load the image from the URL
            Image image = originalIcon.getImage(); // Get the Image object from the ImageIcon

            // Resize the image (e.g., 100x100 is the desired size)
            Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Rescale to desired width and height

            // Create a new ImageIcon with the scaled image
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            photoLabel.setIcon(scaledIcon); // Set the resized image icon to the label
        } catch (Exception e) {
            System.out.println("Error loading icon: " + e.getMessage());
            e.printStackTrace(); // Handle any errors (e.g., URL not found)
        }

        photoLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the image horizontally
        photoLabel.setVerticalAlignment(SwingConstants.TOP); // Center the image vertically
        gbc.gridx = 0; // Position the image at the top center
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Make the image span across 2 columns
        innerPanel.add(photoLabel, gbc);

        // Add the labels for the text fields
        gbc.gridwidth = 1; // Reset gridwidth to 1 for subsequent components
        JLabel tempCondition = new JLabel("Condition:", SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 2;
        innerPanel.add(tempCondition, gbc);

        JLabel tempLabel = new JLabel("Temperature(�F):", SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 3;
        innerPanel.add(tempLabel, gbc);

        JLabel humidityLabel = new JLabel("Humidity(%):", SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 4;
        innerPanel.add(humidityLabel, gbc);

        JLabel windLabel = new JLabel("Wind Speed(m/s):", SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 5;
        innerPanel.add(windLabel, gbc);
        
        JLabel precLabel = new JLabel("Precipitation(mm):", SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 6;
        innerPanel.add(precLabel, gbc);

        // Add the text fields for user input
        conditionField = new JTextField(10);
        conditionField.setEditable(false);
        conditionField.setText(this.condition);
        gbc.gridx = 1;
        gbc.gridy = 2;
        innerPanel.add(conditionField, gbc);

        tempField = new JTextField(10);
        tempField.setEditable(false);
        tempField.setText(Double.toString(temp));
        gbc.gridx = 1;
        gbc.gridy = 3;
        innerPanel.add(tempField, gbc);

        humidityField = new JTextField(10);
        humidityField.setEditable(false);
        humidityField.setText(Double.toString(humidity));
        gbc.gridx = 1;
        gbc.gridy = 4;
        innerPanel.add(humidityField, gbc);

        windField = new JTextField(10);
        windField.setEditable(false);
        windField.setText(Double.toString(windSpeed));
        gbc.gridx = 1;
        gbc.gridy = 5;
        innerPanel.add(windField, gbc);
        
        precField = new JTextField(10);
        precField.setEditable(false);
        precField.setText(Double.toString(precipitation));
        gbc.gridx = 1;
        gbc.gridy = 6;
        innerPanel.add(precField, gbc);

        // Add the inner panel to the main panel
        panel.add(innerPanel, BorderLayout.CENTER);
    }


    //////////////////////////////////////////////////////////////////////
    ///////////////////////////Panel 3////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    
    private void IntializeWeatherParamters(JPanel panel) {
	   panel.setLayout(new GridLayout(5, 2, 10, 10)); // Grid layout with 5 rows and 2 columns for neat alignment

	    // Wind speed
	    JLabel windLabel = new JLabel("Wind Speed Cutoff(m/s):", SwingConstants.CENTER);
	    JTextField windField = new JTextField();

	    // Temperature minimum
	    JLabel tempMinLabel = new JLabel("Temperature Minimum(�F):", SwingConstants.CENTER);
	    JTextField tempMinField = new JTextField();

	    // Temperature maximum
	    JLabel tempMaxLabel = new JLabel("Temperature Maximum(�F):", SwingConstants.CENTER);
	    JTextField tempMaxField = new JTextField();

	    // Rain chance
	    JLabel rainTotalLabel = new JLabel("Precipitation(mm):", SwingConstants.CENTER);
	    JTextField rainTotalField = new JTextField();

	    // Submit button
	    JButton submitButton = new JButton("Save Parameters");

	    // Add components to the panel
	    panel.add(windLabel);
	    panel.add(windField);
	    panel.add(new JLabel()); // Spacer
	    panel.add(tempMinLabel);
	    panel.add(tempMinField);
	    panel.add(new JLabel()); // Spacer
	    panel.add(tempMaxLabel);
	    panel.add(tempMaxField);
	    panel.add(new JLabel()); // Spacer
	    panel.add(rainTotalLabel);
	    panel.add(rainTotalField);
	    panel.add(new JLabel()); // Spacer
	    panel.add(submitButton);

	    // Add action listener to the submit button
	    submitButton.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            try {
	                // Read and validate inputs
	                int windSpeed = Integer.parseInt(windField.getText().trim());
	                int tempMin = Integer.parseInt(tempMinField.getText().trim());
	                int tempMax = Integer.parseInt(tempMaxField.getText().trim());
	                int rainTotal = Integer.parseInt(rainTotalField.getText().trim());

	                // Validate ranges (example ranges, adjust as needed)
	                if (windSpeed < 0 || tempMin < -100 || tempMax > 150 || rainTotal < 0 || rainTotal > 1000) {
	                    JOptionPane.showMessageDialog(panel, "Invalid input values. Please check the ranges.");
	                    return;
	                }

	                // Save the data to a file
	                saveAlertParameters(windSpeed, tempMin, tempMax, rainTotal);

	                JOptionPane.showMessageDialog(panel, "Alert parameters saved successfully!");
	            } catch (NumberFormatException ex) {
	                JOptionPane.showMessageDialog(panel, "Please enter valid integer values.");
	            } catch (IOException ex) {
	                JOptionPane.showMessageDialog(panel, "Error saving alert parameters: " + ex.getMessage());
	            }
	        }
	    });
    }

    private void saveAlertParameters(int windSpeed, int tempMin, int tempMax, int rainTotal) throws IOException {
        this.windLimi = windSpeed;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.rainLimit = rainTotal;
       
    }
    
    //////////////////////////////////////////////////////////////////////
    ///////////////////////////Panel 4////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    private void IntializerService(JPanel panel) {
        panel.setLayout(new BorderLayout(10, 10)); // Set layout for better organization

        // Top section for location input
        JPanel topPanel = new JPanel(new FlowLayout());

        // Create the title label
        JLabel titleLabel = new JLabel("Turn on/off SMS Alerts", SwingConstants.CENTER); 
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Set a larger font size (18 is just an example)

        // Add the title label to the topPanel
        topPanel.add(titleLabel);
        panel.add(topPanel, BorderLayout.NORTH);

        // Bottom section for the switch
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        // Create a toggle button styled as a switch
        JToggleButton switchButton = new JToggleButton("OFF");
        switchButton.setPreferredSize(new Dimension(100, 50));
        switchButton.setBackground(Color.RED); // Default to OFF

        // Add action listener for switch
        switchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (switchButton.isSelected()) {
                    switchButton.setText("ON");
                    switchButton.setBackground(Color.GREEN);

                    // Initialize and schedule the timer
                    timer = new Timer();
                    task = new TimerTask() {
                        @Override
                        public void run() {
                            // Weather-related logic
                            weatherInfo newWeather = new weatherInfo(city);
                            String report = "";
                            if (newWeather.temp > tempMax) {
                                report += "Temperature is extremely high. Seek shade and water! ";
                            }
                            if (newWeather.temp < tempMin) {
                                report += "Temperature is extremely cold. Seek warmth and shelter! ";
                            }
                            if (windLimi < newWeather.windSpeed) {
                                report += "Wind speed potentially dangerous. Seek shelter! ";
                            }
                            if (newWeather.isThunder) {
                                report += "Thunderstorm warning! Seek shelter! ";
                            }
                            if (newWeather.precipitation > rainLimit) {
                                report += "Flood Warning! Seek higher ground! ";
                            }
                            if ("Tornado".equals(newWeather.condition)) {
                                report += "Tornado in area. Seek shelter!!!";
                            }
                            
                      
                        	temp = newWeather.temp;
                        	iconUrl = newWeather.iconUrl;
                        	windSpeed = newWeather.windSpeed;
                        	condition = newWeather.condition;
                        	precipitation = newWeather.precipitation;
                        	feelsLike = newWeather.feelsLike;
                        	humidity = newWeather.humidity;
                        	isThunder = newWeather.isThunder;
                        	
                        	//SETTING PANEL 2 TO NEW INFORMATION
                        	conditionField.setText(condition);
                        	tempField.setText(Double.toString(temp));
                        	humidityField.setText(Double.toString(humidity));
                        	windField.setText(Double.toString(windSpeed));
                        	precField.setText(Double.toString(precipitation));
                        	 try {
                                 // Construct the icon URL (assuming it's already provided in the correct format)
                                 URL url = new URL(iconUrl);  // Ensure iconUrl is a valid URL string
                                 ImageIcon originalIcon = new ImageIcon(url); // Load the image from the URL
                                 Image image = originalIcon.getImage(); // Get the Image object from the ImageIcon

                                 // Resize the image (e.g., 100x100 is the desired size)
                                 Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Rescale to desired width and height

                                 // Create a new ImageIcon with the scaled image
                                 ImageIcon scaledIcon = new ImageIcon(scaledImage);

                                 photoLabel.setIcon(scaledIcon); // Set the resized image icon to the label
                             } catch (Exception e) {
                                 System.out.println("Error loading icon: " + e.getMessage());
                                 e.printStackTrace(); // Handle any errors (e.g., URL not found)
                             }
                        	sendTexts(report);
                            System.out.println(report);
                        }
                    };
                    timer.scheduleAtFixedRate(task, 0, 3600000); // Run every hour
                } else {
                    switchButton.setText("OFF");
                    switchButton.setBackground(Color.RED);

                    // Cancel the timer and task
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    if (task != null) {
                        task.cancel();
                        task = null;
                    }
                }
            }
        });

        bottomPanel.add(switchButton);
        panel.add(bottomPanel, BorderLayout.CENTER);
    }
    
    
    //////////////////////////////////////////////////////////////
    ////////////////TEXT SERVICES/////////////////////////////////
    //////////////////////////////////////////////////////////////
    private void sendTexts(String alertMessage)
    {
    	final String ACCOUNT_SID = "";
    	final String AUTH_TOKEN = "";
    	
    	String filePath = "phone_numbers.txt";
    	List<String> phoneNumbers = new ArrayList<>(); 

    	//grab phone numbers
    	try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
    	    String line;
    	    while ((line = br.readLine()) != null) {
    	        phoneNumbers.add(line.trim()); 
    	    }
    	} catch (Exception e) {
    	    System.err.println("Error reading phone numbers: " + e.getMessage());
    	}
    
    	 // Initialize Twilio
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        for (String phoneNumber : phoneNumbers) {
            try {
                Message message = Message.creator(
                        new PhoneNumber(phoneNumber), 
                        new PhoneNumber("+1111111"), // TWILIO Number
                        alertMessage)
                    .create();

                System.out.println("Message sent to " + phoneNumber + " with SID: " + message.getSid());
            } catch (Exception e) {
                System.err.println("Failed to send message to " + phoneNumber + ": " + e.getMessage());
            }
        }

 
    }
	 
}

class weatherInfo
{
    private static final String API_KEY = ""; // Replace with your OpenWeatherAPI key
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";
	
	public static double temp;
	public static double windSpeed;
	public static String condition;
	public static double precipitation;
	public static double feelsLike;
	public static int humidity;
	public static boolean isThunder = false;
	public static String iconUrl;
	weatherInfo(String city)
	{
		
		 // String city = "Jefferson City, TN, USA";  // City name with spaces and commas

	        String url = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=imperial"; // For temperature in Fahrenheit

			// Now make the HTTP request as before
			try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			    HttpGet request = new HttpGet(url);
			    CloseableHttpResponse response = httpClient.execute(request);

			    if (response.getStatusLine().getStatusCode() == 200) {
			        String json = EntityUtils.toString(response.getEntity());
			        parseWeatherResponse(json);
			    } else {
			        System.out.println("Error: " + response.getStatusLine().getStatusCode());
			    }

			} catch (IOException e) {
			    e.printStackTrace();
			}
	    }
	private static void parseWeatherResponse(String json) {
	    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

	    // City name
	    String cityName = jsonObject.get("name").getAsString();

	    // Main weather data
	    JsonObject main = jsonObject.getAsJsonObject("main");
	    temp = main.get("temp").getAsDouble();
	    feelsLike = main.get("feels_like").getAsDouble();
	    humidity = main.get("humidity").getAsInt();

	    // Wind data
	    JsonObject wind = jsonObject.getAsJsonObject("wind");
	    windSpeed = wind.get("speed").getAsDouble();

	    // Precipitation data (if available)
	    JsonObject rain = jsonObject.getAsJsonObject("rain");
	    precipitation = rain != null && rain.has("1h") ? rain.get("1h").getAsDouble() : 0.0; // Precipitation in last hour

	    // Weather conditions (to check for thunderstorms)
	    JsonArray weatherArray = jsonObject.getAsJsonArray("weather");
	    condition = weatherArray.get(0).getAsJsonObject().get("main").getAsString();

	    // Icon ID
	    String iconId = weatherArray.get(0).getAsJsonObject().get("icon").getAsString();
	    iconUrl = "http://openweathermap.org/img/wn/" + iconId + ".png"; // Construct icon URL
	    
      

	    // Print all data
	    System.out.println("City: " + cityName);
	    System.out.println("Temperature: " + temp + "�F");
	    System.out.println("Feels Like: " + feelsLike + "�F");
	    System.out.println("Humidity: " + humidity + "%");
	    System.out.println("Wind Speed: " + windSpeed + " m/s");
	    System.out.println("Precipitation (last hour): " + precipitation + " mm");
	    System.out.println("Weather Condition: " + condition);
	    System.out.println("Weather Icon URL: " + iconUrl);  // Output the icon URL

	    // Check if it's thunderstorming
	    if ("Thunderstorm".equalsIgnoreCase(condition)) {
	        isThunder = true;
	        System.out.println("It is currently thunderstorming!");
	    } else {
	        isThunder = false;
	    }

	   
	}
}



public class CSC406_JackOHare_0589474_FP {
    public static final String ACCOUNT_SID = "";
    public static final String AUTH_TOKEN = "";
    
public static void main(String[] args) throws UnsupportedEncodingException {

	
	
	Scanner input = new Scanner(System.in);
	
	System.out.println("What is the first place you want to visit?");
	System.out.println("EX: Jefferson City, TN, USA");
	String tempCity = input.next();
	GUIBasics gui = new GUIBasics(tempCity);
	gui.setVisible(true);

	input.close();
      
      
      
    	
    	
    }
   
}