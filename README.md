# android-birthday-tracker
Birthday Tracker &amp; Reminder
Demo: https://youtu.be/4S8SibNlc3Y
Firebase Account: birthday.tracker.app.noreply@gmail.com
Technologies: Android SDK, Java 8, ROOM, Firebase, Dagger (DI)

Description:
- Main use of the application is to store birthdays, the application calculates days remaining and pushes notifications when you enter the application and there's a birhtday in the specific day.
- Birthdays are stored as a picture, birthdate, name and optionally a phone number
- Birthdays can also be deleted
- It also allows the users to synchronize de stored birthdays between various devices
- Allows both Social Login and Mail Login

Implementation details:
- Camera use to  set a profile picture on birthdays
- RecyclerView with user's saved birthdays along with their specific data
- BottomNavigation menu for the application
- Android ShareSheet to share the list of birthdays in text format through different ways
- ObjectAnimator for synchronization animation
- Social Login with Google implemented through Firebase
- ROOM (local database)
- Firebase (Web services / Remote repository)
- Dagger (dependency injection)
