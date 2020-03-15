# Smash-App
Android smash bros helper app that display character statistics, generates a random character, and suggests a character for a user based on form answers.

#### Developers, developers, developers:
Reese Benson :: bensonre<br/>
Michael Commins :: mcommins<br/>
Winston Pingnomo :: pingnomw<br/>
Chase Stubblefield :: chasestubb

### Character Recommendation Feature
- A Google Sheet has been used to store values for each character [(read access available here)](https://docs.google.com/spreadsheets/d/1gRyPhu5cNV1dMMUSyNLDU97iNR4m6FJXl9ylqjjHmh0/edit?usp=sharing "Google Sheets").
- As a user answers questions, they will be generating numeric values for each category. 
- A similarity score will be generating by multiplying the user's value for each category by the characters attributes.
  - The top 3 best matches will be recommended to the user
  - Function awaits further definition. [(Notion doc here)](https://www.notion.so/move/Smash-App-9fbb4f103dfd4d4f827ac45409e945e8).
