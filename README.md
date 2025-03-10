# Feedbloom
### Feedback Management System

## Overview
This project is a Feedback Management System designed to streamline the process of submitting, reviewing, and resolving feedback within an organization. The system is built using Spring Boot and provides a robust workflow for users, approvers, and administrators to manage feedback efficiently.

## Features

### 1. User Registration & Authentication

#### 1.1 New User Registration
- **Sign-Up Process:**
  - Users provide a valid email address and a strong password.
  - The system validates the email format and password strength.
- **Email Verification (Optional):**
  - A verification email is sent to the user.
  - The user activates their account by clicking the verification link.
- **Role Assignment:**
  - The Admin assigns roles: User, Approver, or Admin.
  - The user is notified of their role via email.

#### 1.2 User Login
- **Access & Authentication:**
  - Users enter their email and password on the login page.
  - The system validates credentials and generates a JWT (JSON Web Token).
  - The token is securely stored (e.g., in local storage or cookies).
- **Role-Based Redirects:**
  - Users are redirected to their respective dashboards:
    - **Users:** Feedback submission & tracking.
    - **Approvers:** Feedback review & approval.
    - **Admins:** User management, analytics, & system oversight.

### 2. Feedback Submission (User)

#### 2.1 Submitting Feedback
- **Navigation:**
  - Users access the Submit Feedback section.
- **Feedback Form:**
  - Users provide the following details:
    - **Title:** Brief description.
    - **Description:** Detailed explanation.
    - **Category:** Bug, Feature Request, or General.
    - **Priority:** Low, Medium, or High.
    - **Attachments:** Optional file uploads.
  - The system validates input and saves feedback.
  - Feedback status is set to "Submitted".
  - A confirmation message is displayed.
- **Notifications:**
  - An email and in-app notification are sent to the assigned Approver.

### 3. Feedback Tracking & History (User, Approver, Admin)

#### 3.1 Viewing Feedback
- **User View:**
  - Users navigate to "My Feedback".
  - Feedback can be filtered by:
    - **Category:** Bug, Feature Request, General.
    - **Priority:** Low, Medium, High.
    - **Status:** Submitted, Under Review, Resolved, etc.
    - **Keywords:** Search for specific terms.
  - Sorting options: Submission date, priority, or status.
- **Approver/Admin View:**
  - Access all feedback via "All Feedback".
  - Advanced filters: Date range, assigned approver.

#### 3.2 Viewing Feedback Details
- **Timeline View:**
  - Users click feedback items to see:
    - **Status Changes:** Submitted, Under Review, Resolved.
    - **Comments:** Notes from approvers/team members.
    - **Priority Changes:** Updates to the priority level.
    - **Modified By:** User who made the change.
    - **Timestamp:** Date and time of the change.
- **Attachments:**
  - Users can view or download attachments uploaded by the user.

### 4. Feedback Approval Workflow (Approver, Admin)

#### 4.1 Reviewing Feedback
- **Notification:**
  - Approvers/Admins receive email and in-app notifications for new feedback.
- **Review Actions:**
  - **Request More Info:**
    - Adds a comment requesting clarification.
    - Status remains "Pending Approval".
    - The user is notified.
  - **Approve for Resolution:**
    - Assigns feedback to a team member.
    - Status changes to "Under Review".
    - Priority may be updated.
    - The user is notified.
  - **Reject:**
    - Marks feedback as "Rejected" with a reason.
    - The user is notified.

### 5. Feedback Resolution (Admin, Assigned Team Member)

#### 5.1 Resolving Feedback
- **Workflow:**
  - The assigned team member updates feedback progress.
  - Adds comments to provide updates.
- **Mark as Resolved:**
  - Once resolved, the team member changes the status to "Resolved".
  - The system notifies the user.

### 6. Smart Filtering & Search (All Users)

#### 6.1 Search & Filter Options
- **Searchable Fields:**
  - **Title:** Keywords in the title.
  - **Description:** Keywords in the description.
  - **Category:** Bug, Feature Request, General.
  - **Priority:** Low, Medium, High.
  - **Status:** Submitted, Under Review, Resolved, etc.
  - **Date Range:** Filter by submission date.
- **Sorting & Pagination:**
  - Results are sortable by submission date, priority, or status.
  - Pagination is implemented for large datasets.

### 7. Analytics Dashboard (Admin)

#### 7.1 Viewing Analytics
- **Metrics:**
  - The admin views:
    - **Most Common Feedback Topics:** Recurring issues or popular feature requests.
    - **Trending Requests:** Most requested features over time.
    - **Average Resolution Time:** Time taken to resolve feedback.
    - **Feedback Distribution:** Breakdown by category, priority, and status.
- **Visualizations:**
  - Charts and graphs (e.g., bar charts, pie charts, line graphs) are used for data visualization.

### 8. Notifications & Alerts (User, Approver, Admin)

#### 8.1 Triggered Notifications
- **Events:**
  - Notifications are triggered for:
    - New feedback submission.
    - Feedback status changes.
    - Comments added to feedback.
    - Approval requests.
- **Delivery Methods:**
  - **Email Notifications:** Sent via Spring Boot Mail.
  - **In-App Notifications:** Displayed using WebSocket or AJAX polling.

### 9. Security & Access Control

#### 9.1 Authentication
- **JWT-Based Login:**
  - Users log in using JWT-based authentication.
  - Tokens are securely stored and validated.

#### 9.2 Authorization
- **Role-Based Access:**
  - **Users:** Submit and track feedback.
  - **Approvers:** Review and approve/reject feedback.
  - **Admins:** Manage users and feedback.

#### 9.3 Rate Limiting
- **Prevent Abuse:**
  - API calls are rate-limited to prevent abuse.

#### 9.4 Data Encryption
- **Secure Data:**
  - Sensitive data is encrypted in transit and at rest.

### 10. Deployment & Maintenance

#### 10.1 Deployment Strategy
- **Containerization:**
  - The application is Dockerized for easy deployment.

#### 10.2 Maintenance & Monitoring
- **Regular Updates:**
  - The system is regularly updated with new features and bug fixes.
- **Performance Monitoring:**
  - Performance is monitored using tools like Prometheus and Grafana.

### 11. End-to-End Flow Summary
1. User submits feedback.
2. Approver reviews feedback.
3. Feedback is approved/rejected.
4. Resolved feedback is tracked.
5. Analytics provide insights.
6. Notifications keep users informed.
7. Admins manage security & access control.

### 12. Mission
- **Submit Feedback:** Users can write feedback, categorize it (Bug, Feature Request, General), and set priority levels.
- **Feedback History:** Once feedback is submitted, it should show a timeline of changes (status updates, comments, and priority changes).
- **Approval Workflow:** Feedback should go through an approval process (assigned approvers) before being marked as resolved.
- **Smart Filtering:** Users can filter feedback based on category, priority, status, and even keywords.
- **Analytics Dash:** Show the most common feedback topics and highlight trends (like "most requested feature").
- **Built with Spring Boot**

## 13. Flow Chart
![Flow Chart](https://github.com/sams52s/Dynamic_Feedback_MS/blob/dev/sams/FLOW%20CHART.png)

## 14. UML Sequence Diagram
![UML Sequence Diagram](https://github.com/sams52s/Dynamic_Feedback_MS/blob/dev/sams/%20UML%20Sequence%20Diagram.png)

## 15. UML Class Diagram
![UML Class Diagram](https://github.com/sams52s/Dynamic_Feedback_MS/blob/dev/sams/UML%20Class%20Diagram.png)

## 16. Database Schema Design:

### Entities & Relationships

1. **User (`users`)**  
   - Stores user details and authentication credentials.  
   - **Roles:** User, Approver, Admin.  
   - **Relationships:**  
     - A User can submit multiple Feedbacks.  
     - An Admin assigns roles to users.  

2. **Feedback (`feedbacks`)**  
   - Captures user-submitted feedback with categories, priority, and status.  
   - **Relationships:**  
     - A User submits Feedback.  
     - An Approver reviews Feedback.  
     - A Team Member resolves Feedback.  

3. **Feedback History (`feedback_history`)**  
   - Tracks feedback status changes (audit log).  
   - Stores timestamps and the User who made changes.  

4. **Comments (`feedback_comments`)**  
   - Stores discussions and additional info on feedback.  
   - Related to `feedback_id` and `user_id`.  

5. **Attachments (`feedback_attachments`)**  
   - Stores uploaded files related to feedback.  

6. **Notifications (`notifications`)**  
   - Manages in-app and email notifications for users.  

7. **Roles & Permissions (`roles`, `user_roles`)**  
   - Implements role-based access control (RBAC).  

8. **Analytics & Reports (`feedback_stats`)**  
   - Stores aggregated feedback data for quick dashboard queries.
   - 
## 17. ER Diagram Representation:
![ER Diagram](https://github.com/sams52s/Dynamic_Feedback_MS/blob/dev/sams/ER%20Diagram.png)
