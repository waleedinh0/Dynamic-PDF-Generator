# Dynamic PDF Generation

## Overview

This project is a **Spring Boot** application that provides a **REST API** to generate PDFs dynamically using Java template engines such as **iText**. The application allows users to:

- Generate a PDF based on the provided data.
- Download the generated PDF.
- Store the generated PDF locally and re-download it when the same data is provided.

## Features

- **REST API**: The API accepts structured input data (JSON format) and generates a PDF.
- **Local Storage**: PDFs are stored locally, and if the same data is provided again, the previously generated PDF is returned to avoid redundant generation.
- **PDF Download**: The generated PDF can be downloaded via the API.

## Technologies Used

- **Spring Boot**: Backend framework for building the REST API.
- **Java Template Engine**: iText is used for PDF generation.
- **JUnit**: Used for writing unit tests for the application.
- **Maven**: Project management and build tool.
