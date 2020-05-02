# Recipes App by Steven McKeown
The app allows you to login in 3 ways: Email and Password, Phone login, Google Login.
Once logged in you are brought to the home activity where some information about the app is displayed.
In the home screen you have access to the nav drawer where you can then navigate to the Add a recipe screen or the Your Recipes screen.
A logout action is also available in the options menu from the home screen.

The ‘Add a recipe’ screen displays a stock image which you can change via the Add Image button. You must set a title for the recipe otherwise you cannot add it. A description is optional. I used a multi-line editText for the recipe description. Add Recipe button adds the title, description and user chosen image. Optionally if you don’t wish to add anything you can hit the cancel button in the top right of the screen.

Once you add a recipe you are returned to the home screen with the nav drawer. You can then view your added recipes in ‘Your Recipes’ in the nav drawer. In the Your Recipe screen you can view your added recipes which are then stored in a JSON file. The CardView layout is able to expand depending on the amount of information entered in the recipe description. You are able to tap any of the cards and then edit any detail you wish, including title, description and image. You also have the option to delete that recipe or exit off the edit screen via the bin and cancel icons in the top right.

Once in either ‘Add a Recipe’ or ‘Your Recipes’ screen you must press the back button on your phone to navigate out of them and back to the Home screen.

Known errors:
The logout option in the option menu causes the app to crash if you use Google sign-in and crashes for the other sign-in options if you login and logout multiple times. This can be circumvented by pressing the back button on your phone while on the home screen which brings you back to the default logout button.

You can press the back button on your phone in the home screen and be brought back to a standalone logout button.

References
A lot of the code in this project was based on both the Placemark application and the Recipes Application.

For login using Firebase I used this video to help me: https://www.youtube.com/watch?v=7SZO3bT1M0I&t=418s
I edited the onActivityResult method to redirect you to the Home activity as long as there is a user signed in.

