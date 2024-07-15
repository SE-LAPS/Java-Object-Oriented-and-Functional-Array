# 🌟 Introduction
In this assignment, you'll practice your skills in Java programming, focusing on both object-oriented and functional programming paradigms. You'll implement a set of classes and methods to manipulate arrays, demonstrating your understanding of key concepts.

## ✨ Features
- Object-oriented programming with Java classes and methods.
- Functional programming using Java's Stream API.
- Comprehensive test cases to validate your code.
- Clear and concise documentation.

## 🛠️ Requirements
- Java Development Kit (JDK) 8 or higher
- Integrated Development Environment (IDE) like IntelliJ IDEA, Eclipse, or VSCode
  
## 💻 Installation
- Clone the repository:
https://github.com/SE-LAPS/Java-Object-Oriented-and-Functional-Array/tree/main?tab=readme-ov-file

- Navigate to the project directory:
cd assignment-1-java-array

- Open the project in your favorite IDE.
  
## 🚀 Usage

- Compile the Java files:
javac src/com/yourpackage/*.java
java com.yourpackage.Main

- Run the tests:
java -cp .:junit-4.13.2.jar org.junit.runner.JUnitCore com.yourpackage.YourTestClass

## 🧩 Examples
Here's an example of how to use the classes and methods you will implement:

java

// Create an instance of your array manipulation class
ArrayManipulator manipulator = new ArrayManipulator(new int[]{1, 2, 3, 4, 5});

// Perform some operations
manipulator.reverseArray();
manipulator.sortArray();
int sum = manipulator.sumArray();

// Print the results
System.out.println("Reversed Array: " + Arrays.toString(manipulator.getArray()));
System.out.println("Sorted Array: " + Arrays.toString(manipulator.getArray()));
System.out.println("Sum of Array: " + sum);

## 🤝 Contributing
Contributions are welcome! Please follow these steps to contribute:

- Fork this repository.
- Create a new branch with your feature or bugfix.
- Commit your changes.
- Push to your branch.
- Create a new Pull Request.


## 📄 License
This project is licensed under the MIT License. See the LICENSE file for more details.

## 📧 Contact
For any questions or suggestions, please feel free to open an issue or reach out to me at your.email@example.com.
