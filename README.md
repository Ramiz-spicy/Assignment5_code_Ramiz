# Assignment 5 – Unit, Mocking, and Integration Testing

## Project Overview
For this project, I worked on writing unit, mocking, and integration tests for components in the Amazon and Barnes & Noble systems.  
My main goal was to make sure that both individual classes and their interactions work correctly, while keeping the code easy to maintain and extend.  

I also set up a GitHub Actions workflow that automatically builds the project, runs tests, and checks code style using Maven, JUnit, and Checkstyle.

---

## Continuous Integration Workflow
The workflow runs automatically every time I push changes to the `main` branch.

**Workflow Link:**  
[GitHub Actions Workflow](https://github.com/Ramiz-spicy/Assignment5_code_Ramiz/actions)

Each run of the workflow does the following:
- Compiles the project  
- Runs all unit and integration tests  
- Checks code style with Checkstyle  
- Generates coverage and analysis reports  

---

## Build Status
All of my recent workflow runs have passed successfully.  
You can confirm this by checking the latest GitHub Actions run here:  



