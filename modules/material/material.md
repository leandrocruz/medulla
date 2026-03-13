# Medulla Material - Material Design 3 for Laminar

Scala.js/Laminar wrappers for [Material Web Components](https://github.com/niclasko/niclasko.github.io) (`@material/web`).

## Setup

### 1. Add the SBT dependency

In your `build.sbt`, add the `medulla-material` module as a dependency:

```scala
lazy val myApp = project.in(file("modules/myapp"))
  .enablePlugins(ScalaJSPlugin)
  .settings(...)
  .dependsOn(material)
```

### 2. Install the npm package

```bash
npm install @material/web
```

### 3. Import components in JavaScript

In your JS entry point (e.g., `main.js`), import the components you need:

```js
import './tailwind.css'
import './style.css'

// Material Web Components
import '@material/web/button/filled-button.js'
import '@material/web/button/outlined-button.js'
import '@material/web/button/text-button.js'
import '@material/web/button/elevated-button.js'
import '@material/web/button/filled-tonal-button.js'
import '@material/web/checkbox/checkbox.js'
import '@material/web/dialog/dialog.js'
import '@material/web/icon/icon.js'
import '@material/web/textfield/filled-text-field.js'
import '@material/web/textfield/outlined-text-field.js'
// ... add more as needed

import 'scalajs:main.js'
```

Only import the components you actually use to keep your bundle small.

### 4. Add the Material Symbols font (for icons)

In your `index.html`:

```html
<link href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" rel="stylesheet" />
```

### 5. Set up the Material theme (optional)

Add CSS custom properties to your stylesheet to customize the theme:

```css
:root {
  --md-sys-color-primary: #6750a4;
  --md-sys-color-on-primary: #ffffff;
  --md-sys-color-surface: #fffbfe;
  /* See https://m3.material.io/develop/web for all tokens */
}
```

## Usage

Import the wrappers in your Scala.js code:

```scala
import medulla.material.*
import com.raquo.laminar.api.L.*
```

### Buttons

```scala
MdButton.filled("Save")
MdButton.outlined("Cancel", onClick --> cancelObserver)
MdButton.text("Learn more")
MdButton.elevated("Upload")
MdButton.filledTonal("Draft")

// With icon
MdButton.filled(MdIcon("send"), "Send")

// Trailing icon
MdButton.filled(MdButton.trailingIcon := true, "Next", MdIcon("arrow_forward"))

// Disabled
MdButton.filled(Md.disabled := true, "Unavailable")
```

### Icon Buttons

```scala
MdIconButton(MdIcon("settings"))
MdIconButton.filled(MdIcon("edit"))
MdIconButton.filledTonal(MdIcon("favorite"))
MdIconButton.outlined(MdIcon("share"))
```

### Floating Action Buttons

```scala
MdFab(Md.label := "Create", MdIcon("add"))
MdFab(MdFab.size := "small", MdIcon("add"))
MdFab(MdFab.size := "large", MdFab.lowered := true, MdIcon("edit"))
```

### Icons

```scala
MdIcon("home")
MdIcon("settings")
MdIcon("favorite")
```

Icon names come from [Material Symbols](https://fonts.google.com/icons).

### Text Fields

```scala
MdTextField.filled(Md.label := "Name")
MdTextField.outlined(Md.label := "Email", MdTextField.textFieldType := "email")

// With validation
MdTextField.filled(
  Md.label := "Password",
  MdTextField.textFieldType := "password",
  MdTextField.required := true,
  MdTextField.minLength := 8,
  Md.supportingText := "At least 8 characters"
)

// With prefix/suffix
MdTextField.outlined(
  Md.label := "Amount",
  MdTextField.prefixText := "$",
  MdTextField.textFieldType := "number"
)

// Textarea
MdTextField.filled(
  Md.label := "Description",
  MdTextField.textFieldType := "textarea",
  MdTextField.rows := 3
)

// Error state
MdTextField.filled(
  Md.label := "Username",
  MdTextField.error := true,
  MdTextField.errorText := "Username already taken"
)
```

### Checkbox

```scala
MdCheckbox()
MdCheckbox(MdCheckbox.checked := true)
MdCheckbox(Md.disabled := true)
MdCheckbox(MdCheckbox.indeterminate := true)

// With label (use standard HTML label)
label("Accept terms", MdCheckbox(), " I agree")
```

### Radio

```scala
MdRadio(nameAttr := "size", value := "small")
MdRadio(nameAttr := "size", value := "large", MdRadio.checked := true)
```

### Switch

```scala
MdSwitch()
MdSwitch(Md.selected := true)
MdSwitch(MdSwitch.icons := true)
MdSwitch(Md.disabled := true)
```

### Select

```scala
MdSelect.filled(
  Md.label := "Country",
  MdSelectOption(value := "br", "Brazil"),
  MdSelectOption(value := "us", "United States"),
  MdSelectOption(value := "uk", "United Kingdom"),
)

MdSelect.outlined(
  Md.label := "Role",
  MdTextField.required := true,
  MdSelectOption(value := "admin", "Admin"),
  MdSelectOption(value := "user", "User"),
)
```

### Dialog

```scala
val isOpen = Var(false)

div(
  MdButton.filled("Open Dialog", onClick --> { _ => isOpen.set(true) }),
  MdDialog(
    MdDialog.open <-- isOpen.signal,
    MdDialog.onClosed --> { _ => isOpen.set(false) },
    div(slotAttr := "headline", "Confirm"),
    div(slotAttr := "content", "Are you sure?"),
    div(
      slotAttr := "actions",
      MdButton.text("Cancel", onClick --> { _ => isOpen.set(false) }),
      MdButton.filled("Confirm", onClick --> confirmObserver),
    )
  )
)
```

### Chips

```scala
MdChipSet(
  MdChip.assist(Md.label := "Help"),
  MdChip.filter(Md.label := "Tag 1"),
  MdChip.filter(Md.label := "Tag 2", Md.selected := true),
  MdChip.input(Md.label := "Removable", MdChip.removable := true),
  MdChip.suggestion(Md.label := "Suggestion"),
)
```

### List

```scala
MdList(
  MdListItem(Md.headline := "Item 1", Md.supportingText := "Description"),
  MdListItem(Md.headline := "Item 2"),
  MdListItem(Md.headline := "Item 3"),
)
```

### Menu

```scala
div(
  position.relative,
  MdButton.filled(idAttr := "menu-anchor", "Open Menu"),
  MdMenu(
    MdMenu.anchor := "menu-anchor",
    MdMenu.open <-- menuOpen.signal,
    MdMenu.onClosed --> { _ => menuOpen.set(false) },
    MdMenuItem(Md.headline := "Cut"),
    MdMenuItem(Md.headline := "Copy"),
    MdMenuItem(Md.headline := "Paste"),
  )
)
```

### Progress

```scala
// Indeterminate
MdProgress.linear(MdProgress.indeterminate := true)
MdProgress.circular(MdProgress.indeterminate := true)

// Determinate (value from 0 to 1)
MdProgress.linear(MdProgress.progressValue := "0.5")
MdProgress.circular(MdProgress.progressValue := "0.75")
```

### Tabs

```scala
MdTabs(
  MdTab.primary(MdIcon("home"), "Home"),
  MdTab.primary(MdIcon("settings"), "Settings"),
  MdTab.primary(MdIcon("info"), "About"),
  MdTabs.onchange --> { _ => /* handle tab change */ },
)

MdTabs(
  MdTab.secondary("Recent"),
  MdTab.secondary("Favorites"),
)
```

### Divider

```scala
MdDivider()
MdDivider(MdDivider.inset := true)
```

### Ripple and Elevation

```scala
// Add ripple to a custom container
div(position.relative, MdRipple(), "Click me")

// Add elevation
div(position.relative, MdElevation())
```

## Shared attributes (`Md` object)

The `Md` object contains attributes and events common across multiple components:

| Attribute | Type | Description |
|-----------|------|-------------|
| `Md.disabled` | `Boolean` | Disables the component |
| `Md.selected` | `Boolean` | Marks as selected |
| `Md.label` | `String` | Label text |
| `Md.headline` | `String` | Headline text (lists, menus) |
| `Md.supportingText` | `String` | Supporting/helper text |
| `Md.hasIcon` | `Boolean` | Indicates an icon is present |

| Event | Description |
|-------|-------------|
| `Md.onOpen` | Fires when component opens |
| `Md.onClose` | Fires when component closes |
| `Md.onChange` | Fires on value change |
| `Md.onInput` | Fires on input |

## Reactive bindings

Since these are standard Laminar elements, all reactive bindings work as expected:

```scala
val name = Var("")

MdTextField.filled(
  Md.label := "Name",
  value <-- name.signal,
  onInput.mapToValue --> name.writer,
)

MdButton.filled(
  Md.disabled <-- name.signal.map(_.isEmpty),
  "Submit"
)
```

## Available components

| Wrapper | JS Import | Tag(s) |
|---------|-----------|--------|
| `MdButton` | `@material/web/button/*.js` | `md-filled-button`, `md-outlined-button`, `md-text-button`, `md-elevated-button`, `md-filled-tonal-button` |
| `MdIconButton` | `@material/web/iconbutton/*.js` | `md-icon-button`, `md-filled-icon-button`, `md-filled-tonal-icon-button`, `md-outlined-icon-button` |
| `MdFab` | `@material/web/fab/*.js` | `md-fab`, `md-branded-fab` |
| `MdIcon` | `@material/web/icon/icon.js` | `md-icon` |
| `MdCheckbox` | `@material/web/checkbox/checkbox.js` | `md-checkbox` |
| `MdRadio` | `@material/web/radio/radio.js` | `md-radio` |
| `MdSwitch` | `@material/web/switch/switch.js` | `md-switch` |
| `MdTextField` | `@material/web/textfield/*.js` | `md-filled-text-field`, `md-outlined-text-field` |
| `MdSelect` | `@material/web/select/*.js` | `md-filled-select`, `md-outlined-select` |
| `MdSelectOption` | `@material/web/select/select-option.js` | `md-select-option` |
| `MdDialog` | `@material/web/dialog/dialog.js` | `md-dialog` |
| `MdChipSet` | `@material/web/chips/chip-set.js` | `md-chip-set` |
| `MdChip` | `@material/web/chips/*.js` | `md-assist-chip`, `md-filter-chip`, `md-input-chip`, `md-suggestion-chip` |
| `MdList` | `@material/web/list/list.js` | `md-list` |
| `MdListItem` | `@material/web/list/list-item.js` | `md-list-item` |
| `MdMenu` | `@material/web/menu/menu.js` | `md-menu` |
| `MdMenuItem` | `@material/web/menu/menu-item.js` | `md-menu-item` |
| `MdSubMenu` | `@material/web/menu/sub-menu.js` | `md-sub-menu` |
| `MdProgress` | `@material/web/progress/*.js` | `md-linear-progress`, `md-circular-progress` |
| `MdTabs` | `@material/web/tabs/tabs.js` | `md-tabs` |
| `MdTab` | `@material/web/tabs/*.js` | `md-primary-tab`, `md-secondary-tab` |
| `MdNavigationBar` | `@material/web/navigationbar/*.js` | `md-navigation-bar` |
| `MdNavigationTab` | `@material/web/navigationtab/*.js` | `md-navigation-tab` |
| `MdNavigationDrawer` | `@material/web/navigationdrawer/*.js` | `md-navigation-drawer` |
| `MdRipple` | `@material/web/ripple/ripple.js` | `md-ripple` |
| `MdElevation` | `@material/web/elevation/elevation.js` | `md-elevation` |
| `MdDivider` | `@material/web/divider/divider.js` | `md-divider` |
