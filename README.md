# Techdegree Project 6 

Analyze Public Data with Hibernate | Written in Java & Hibernate

Code files: /src/main/java

## Minimum Viable Product:
- In the IDE of your choice, create a Gradle project and include the standard directory structure for a Java application.
- In the Gradle build file, add the following dependencies:
  - Hibernate core
  - Java Transaction API (JTA)
- Create a data directory in your project. Save the downloaded H2 database in this directory.
- Create a model class that can be associated with the table of the given database. The data in the provided database includes one table named Country, and each row in that table contains the following columns:
  - code: VARCHAR(3) - this is the primary key, a String with a maximum length of 3 characters
  - name: VARCHAR(32) - a String with a maximum length of 32 characters
  - internetUsers: DECIMAL(11,8) - A number with a maximum length of 11 digits and 8 digits of decimal precision
  - adultLiteracyRate: DECIMAL(11,8) - A number with a maximum length of 11 digits and 8 digits of decimal precision
- The username for the supplied database is “sa” and there is no password.
- Write the application code for a console application that allows a user to view a list of well-formatted data for all countries. This should be formatted in columns, contain column headings, and numbers should be rounded to the nearest hundredth. For any data that is unreported (NULL in the database), this should be clear in the displayed table. Please reference the provided example for data formatting.
- Add to your console application the code that allows a user to view a list of statistics for each indicator, including (but not limited to) a maximum and minimum for each indicator, and a correlation coefficient for the two indicators together. You may use a third-party library to calculate the correlation coefficient. Keep in mind that all calculated statistics should exclude any country that doesn’t have data reported for the indicators under analysis (instead of using zero for missing values).
- Write the application code that allows a user to edit a country’s data.
- Write the application code that allows a user to add a country with data for each indicator.
- Write the application code that allows a user to delete a country’s data.

## Extra Credit:
- Calculate a correlation coefficient between the two indicators without using a third-party library.
- Use the builder pattern for creating new country objects.
- Use Java streams for finding maxima and minima.
