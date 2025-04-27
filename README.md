# Login Information
username: test,
password: password

# Features completed for Sprint 1:
- Login/Sign Up
- Logout button on the Profile Tab
- Navigation between Fragments and Activities, including tabs and back arrows for navigation up

# Brief Description of Sprint 1 Implementations
- Closet Landing:
  - Closet Tab:
    - View list of garments in a scrollable grid.
    - Heart garment items to mark as favorites.
  - Saved Outfits Tab:
    - Grid view of saved outfits.
    - Option to edit/delete outfits by clicking on outfit.
- Add/Plan:
  - Plan Outfit:
    - Selects two-three garments, lets the user preview outfit, then saves to closet if user chooses to.
  - Add Item:
    - Add Item prompts the camera and saves the image to the database if the user fills out the listing. 
- Closet landing:
  - Shows a list of garments in the "Closet" tab and allows user to heart their favorite items.
  - Shows a list of saved outfits in the "Saved Outfits" tab and allows the user to edit or delete any listing. 
- Add/Plan:
  - Plan Outfit lets the user pick an outfit based on the items in their closet.
    - The user is required to choose at least two garments, and no more than three. Once the user is done planning, they should press the "Done Planning" button, or they can press the back button to navigate to the previous screen.
    - Once the "Done Planning" button is clicked, the view will navigate to the Outfit Review view. The user can press the back button in the action bar to navigate to the previous screen, or they can click the "Save Outfit" button to save the outfit in their closet.
    - Once the "Save Outfit" button is clicked, the view will navigate back to the Closet landing. If the user clicks on the "Saved Outfits" tab, the newly created outfit will appear.
  - Add Item prompts the camera and saves the image to the database if the user fills out the listing. 

# Features completed for Sprint 2:
- New authentication/user data implemented
- Linking all previous data to new database
- Attaching photos to all instances
- Filtering (thank you Anna!)
- Edit/Delete Outfit/Garment Implemented

# Brief Description of Sprint 2 Implementations
- Database 
  - User data is now organized by usernames
- Garments
  - Bug fixed in garment categorization where the keyboard pushes everything
  - Images fully linked
  - Favoriting implemented, no longer crashes the app
  - Context menu (long click) will allow you to edit or delete item
- Outfits
  - Outfit photos now attached
  - changed limit to 4 for uniform display
  - Users can now edit the names of outfits
  - Long click will allow you to rename or delete outfit

# General state of app:
- Navigating App Features:
  - Closet Landing:
    - Closet Tab:
      - Quick Overview:
        - Heart items to add to favorites, repeat to remove from favorites
        - Click and drag to scroll through garments, navigate to "Saved Outfits" tab, or view categorizations.
      - Detailed Instructions:
        - This is the default view once the app opens, so there is no need to navigate anywhere on the navigation menu.
        - The Closet landing allows you to scroll up/down, left/right (to navigate to "Saved Outfits" tab), and left/right on the garment details at the bottom of each garment image.
        - To add a garment to your favorites, simply press the heart which will immediately fill the heart with pink. To unfavorite it, repeat this action.
        - Filtering: Right now, filtering is cumulative. This means that if you filter by favorites, click off, and try to filter by a color (ex: yellow), the results will show favorites that are also that color. Aside from this, filtering allows users to specify a color, category, or favorite and display items of those tags.
    - Saved Outfits Tab:
      - Quick Overview:
        - Click "Saved Outfits" tab to reach this view.
        - Click on an outfit to see an expanded view which also allows you to edit or delete the outfit.
      - Detailed Instructions:
        - Click on the "Saved Outfits" tab or drag the screen to the left to navigate to the users saved outfits. Here will be a grid view of any saved outfits the user creates.
        - Click on any item to navigate to a detailed view of the outfit. 
        - Once clicking on the item, you will have the option to either "Edit Outfit" or "Delete Listing" or press the back button to return to the previous screen.
        - To edit the outfit, click the "Edit Outfit" button which will navigate back to the garment selection view. If the user decides not to make any edits, press the back button to navigate to the previous screen. Otherwise, follow the same steps as listed in Plan Outfit once again.
        - To delete the outfit from your saved outfits, click the "Delete Listing" button. This will remove the specified outfit to be deleted from the "Saved Outfits" tab and navigate back to the "Saved Outfits" landing.
  - Add/Plan:
    - Plan Outfit:
      - Quick Overview:
        - Go to Add/Plan tab at bottom navigation menu
        - Click Plan Outfit button and choose 2-3 items to plan an outfit and save to closet.
      - Detailed Instructions:
        - To plan an outfit, navigate to the Add/Plan tab at the bottom of the navigation menu.
        - Press the Plan Outfit button which will navigate to the garment selection view.
        - Choose at least two, but up to four, items and when you are satisfied, click Done Planning. This will navigate to the Outfit Review view.
        - If you wish to remove/add an item, press the back button to navigate to the previous screen. Otherwise, click "Save Outfit" which will navigate to the Closet landing.
        - Click on the "Saved Outfits" tab where your newly saved outfit will be.
    - Add Item:
      - Quick Overview:
        - Go to Add/Plan tab at bottom navigation menu
        - Click Add Item button and take a picture of garment, categorize it, and add to the closet.
      - Detailed Instructions:
        - To add an item, navigate to the Add/Plan tab at the bottom of the navigation menu.
        - Press the Add Item button which will navigate to another view which will allow the user to capture an image.
        - Click the camera button to capture the image.
        - After capturing the image, either press the camera button again to retake the image, or press the "Save" button to navigate to the "Garment Listing" view.
        - If you wish to retake the picture after navigating here, simply press the back button. Otherwise, in the "Garment Listing" you will be unable to leave the screen until you describe your item with required categories.
        - Use the dropdown menus to select categories, and the color buttons to describe the color of the garment. After finishing, click "Save" which will navigate to the Closet landing.
        - You will now be able to see your newly added garment in your closet with its appropriate categorizations.

    - This is the default view once the app opens, so there is no need to navigate anywhere on the navigation menu.
    - The Closet landing allows you to scroll up/down, left/right (to navigate to "Saved Outfits" tab), and left/right on the garment details at the bottom of each garment image.
    - To add a garment to your favorites, simply press the heart which will immediately fill the heart with pink. To unfavorite it, repeat this action.
    - Click on the "Saved Outfits" tab or drag the screen to the left to navigate to the users saved outfits. Here will be a grid view of any saved outfits the user creates.
  - Add/Plan:
    - Plan Outfit:
      - To plan an outfit, navigate to the Add/Plan tab at the bottom of the navigation menu.
      - Press the Plan Outfit button which will navigate to the garment selection view.
      - Choose at least two, but up to three, items and when you are satisfied, click Done Planning. This will navigate to the Outfit Review view.
      - If you wish to remove/add an item, press the back button to navigate to the previous screen. Otherwise, click "Save Outfit" which will navigate to the Closet landing.
      - Click on the "Saved Outfits" tab where your newly saved outfit will be.
    - Add Item:
      - To add an item, navigate to the Add/Plan tab at the bottom of the navigation menu.
      - Press the Add Item button which will navigate to another view which will allow the user to capture an image.
      - Click the camera button to capture the image.
      - After capturing the image, either press the camera button again to retake the image, or press the "Save" button to navigate to the "Garment Listing" view.
      - If you wish to retake the picture after navigating here, simply press the back button. Otherwise, in the "Garment Listing" you will be unable to leave the screen until you describe your item with required categories.
      - Use the dropdown menus to select categories, and the color buttons to describe the color of the garment. After finishing, click "Save" which will navigate to the Closet landing.
      - You will now be able to see your newly added garment in your closet with its appropriate categorizations.

# Limitations:
  - Emulators are unable to support taking an actual picture, so there were limitations on the images stored in the app.
  - Could store limited amount of images in JSON, but only a few because of storage issues
  - Some features for storing images required payment

# Sprint 2 Meeting Major Decisions
- A hovering question following sprint 1 was whether we wanted to switch to FireStore instead of realtime database. We ended up staying with realtime as our database is constantly updated and edited
- Displaying outfits proved to be difficult, as we wanted to balance user aesthetic and uniformness. We settled on a 2x2 square formatting.
- We found filtering to be difficult with realtime database. Instead of a query based approach, we used a user data loop as it was quicker and more space efficient.
- Users will not be able to edit outfits, but only the names. 
- We will not be filtering the plan page, due to time constraints. Filters are only applied to closet page

# Main Contributions
- Anna: Filtering (Java and Firebase), Closet (XML and Java), Add (XML and Java), Camera (XML and Java)
- Adeola: Closet (XML and Java), Plan (XML and Java), Add (XML and Java), Profile (XML and Java)
- Emily: Firebase related code (storage, data org., and authentication), Login (Java and XML), Closet (Java)
- Shevante: Closet (XML and Java), Plan (XML and Java), Add (XML and Java), Profile (XML and Java)

