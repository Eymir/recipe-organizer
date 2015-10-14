# Recipe Book #

Recipe Book application.
This could run on a screen in a refrigerator or stand-alone computer on the kitchen counter.

Why?
Lots of recipes, hard to find them
Allows hands-free use of a recipe
don't have to take it out, clean it off, and put it back

# Storage #

Store each recipe as a separate entry
  * separate file per recipe?
    * too much metadata per recipe, too much duplication of data
    * store metadata in each recipe (and runtime database) or in database?
  * database?
    * recipe-id
    * ingredient list
    * instructions (text block?)
      * need some good example recipes to see how it will be stored
    * indexed by recipe name, tag/label, ingredients
  * Fields
    * list of ingredients (qty, unit, kind, preparation), e.g.:
      * 2/large (8oz)/onion/chopped
      * 1/qt/mustard
      * 6/oz/parmesan cheese/grated
    * preparation instructions
    * notes
    * "goes well with" (2-way links to other recipes)
      * subrecipes are automatically added to this list
    * recipe "traits", allows others to automatically be matched to it for a "similar" auto-link
      * something like a hash code where a recipe gets a string compared to others for similarity
    * scan of "original"
    * pictures of prepared meal
Allow links/subrecipes
  * e.g., cake and linked icing
  * links could be inlined or references
  * allows for link networks
Categorization: Labels
  * Labels - give a property name and value to a recipe: "category"->"meat", "temperature"->"350 425" (for multiple)
    * Could do hierarchical as with property based groupings and tail expansion - break on spaces
  * Tags - similar to "tabs" in a recipe binder
    * A tag is a label with a value of "null" or "empty"
    * Could be implemented as boolean properties
    * only requires presence of the tag, e.g.: "poultry"
    * allows for tag clouds
Ingredients
  * list ingredients with highest granularity
  * "original order", and allow other sorting options (alpha, quantity)

# Non-Standard Interface #
Three main UI parts:
  * touch screen
  * voice activation
  * foot pedals

Use a touch screen hardware system, probably with a cheap computer backing it
  * large buttons
  * visual feedback on current operations, active states
  * recipes listed with a lot of (auto and manual) text decorations, e.g. colors for appliance, amount, duration
    * auto-coloration should be done on recipe create, or could be manually initiated later

Voice activation - main means of interaction, coupled with voice response
  * side of the screen, when voice activation enabled, shows a tree of current available commands
    * highlight current, children in the tree are parameters, sub-commands, subsequent commands, etc.
  * wireless bluetooth headset would be be best for a microphone instead of yelling
    * devices should be "friendly named"
    * UI shows what device sent input
    * volume control prominent in UI
  * main commands should all be distinct, e.g.
    * STOP
    * GO
    * REPEAT
    * NEXT
    * BACK
    * COMMANDS (show/hide tree)

Foot pedals - simple USB pushbuttons, could also be wireless like headset
  * like soft buttons on phones
  * area with sub-areas at the bottom of the screen representing the foot pedals (probably 3)
    * usually for back, forward, enter/next

# Interaction Use Cases #
"Entry-point" operations:
  * Search for recipe
  * browse by tag or label
  * add/edit a recipe
    * (automatically searches for "similar")
    * automatically styles text

Once a recipe is found, e.g. selected from search results
  * list the recipe ingredients, preparation instructions, etc. according to input
  * button or specific voice sequence to enable voice activation
  * GO to begin listing ingredients, NEXT to progress to next ingredient, REPEAT, BACK, etc.
  * on NEXT, "end of ingredients list, say GO to begin preparation instructions"
    * same interaction

Add a recipe:
  * voice entry is a must
    * "would you like to speak the recipe to me?"
    * "begin saying the ingredients list"
    * "begin saying the preparation instructions"
  * perhaps OCR to scan in recipes too
  * once the bulk is entered, it will probably need cleanup
    * reorder ingredients, reorder instructions, fix punctuation
  * need spellcheck

# Components/Implementation #
## Overall Architecture ##
Eclipse plug-in workbench with most things turned off (perspective bar, tool bar, view borders) will probably be a quick way to start
Use DION/XML for serialization

Some classes in the core:
Recipe, Traits

Depends on core:
Data store, Controller

Application ties controller to interface
Registers data store manager query service

## UI ##
Eclipse libraries- SWT/JFace/GC

For touch screen, will have to have a separate UI framework that can read the same data store

## Text To Speech ##
http://en.wikipedia.org/wiki/Festival_Speech_Synthesis_System<br />
http://en.wikipedia.org/wiki/FreeTTS<br />
http://freetts.sourceforge.net/docs/index.php

## Data Store ##
Searchable Indecies:
  * title
  * ingredient (+amount)
  * tag/label
  * linked recipes
Allow search of "contains" and "matches"

class Traits
  * composite of indecies and a value for each
  * v1- name, tags, ingredients

class Recipe
  * all fields of a recipe, same pieces of composition used for Traits
  * v2- internal weak reference to larger storage, fetched on access if disposed

data store manager - API between the controllers and storage
  * API: functions-as-queries, return Recipe instances
    * lookupSingle(Traits):Recipe - find a match to the given Traits or null result
    * lookup(Traits):Collection`<Recipe>` - find matches or empty list
    * v2- lookup(Traits, float pct):Collection`<Recipe>` - find matches or empty list within the given match threshold
  * v1- store each recipe as a separate XML file
    * process all on app start and store in-memory indecies
    * when a recipe is requested, follow the index to the file on disk, read, return record structure (weak reference) for display

# Milestone Targets #

v1
  * data store
    * flat directory of XML files, one per recipe, embedded duplicated metadata
    * allow archiving to a single zip/jar with indexes - recipe archive will port to other UIs
  * views (primary perspective - Cookbook)
    * _Recipe Explorer_ view: list of recipes/files (categorized, filtered, custom columns)
    * _Recipe_ view (editor?): selected recipe shows in this view
      * rendered html?
      * controls for play-back are here
    * _Tag Cloud_ view: min to max font size, scale by number of recipes with that label
      * styled text area, styled ranges
      * select a label to filter a new Recipe Explorer view by that tag/label - new view with secondary ID
      * right-click label to contribute to _Filter_ view
    * ~~Label Explorer view~~: show all values for a label and what recipes have that value
      * **Note**: this is the same as the Recipe Explorer, but shows all categorizations? Probably don't need this view
      * 3-tier tree - label name, value, recipe title
      * multi-select to edit values
      * drag and drop
    * _Labels_ view: show labels and values for the current selected Recipe view
    * _Filter_ view: complex filtering options (allow multiple of this view?)
      * if available, contribute view menu item to any "filterable" view to link it
      * linked filterable view will use the filter described in the _Filter_ view
      * statusline contribution for "filter active"
      * filters - undo/redo, history, bookmarks
      * only AND comparisons like, text compares "contains" ignore case
    * Bookmarks
      * filter + filing tuple, opens a new explorer
  * Recipe Editor - secondary perspective
    * File -> New, save, save as, save all, etc. - flush persistence of any edits done to the current recipe(s)
    * views
      * recipe editor - backed by IResource to do undo/redo, save (if to database, save persists runtime delta)
      * recipe label editor (view) - show labels and values for the current selected "recipe editor"
        * add/remove value for label
        * create new label
        * there is no "remove label", remove values from all known recipes to remove the label


v2
  * touch screen UI
  * TTS - research "Microsoft Sam" and built-in windows TTS
  * voice activation
  * labels allow multimap
  * labels allow tail expansion
    * need to consider ugly tag cloud items, etc, everything but filing looks bad with a control character