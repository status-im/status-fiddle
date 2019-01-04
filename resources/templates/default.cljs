[react/view {:style {:flex 1}}
 [toolbar/toolbar {}
  toolbar/default-nav-back
  [toolbar/content-title "Status"]]
 [react/view {:style {:flex 1
                      :align-items :center
                      :justify-content :center}}
  [icons/icon :icons/profile-active]
  [react/text {:style {:font-size 25 :padding 20}}
   "Hello!"]
  [react/text {:style {:padding-horizontal 20}}
   "This is an " (if platform/ios? "iPhone" "Android Phone") ", but you can change device in the listbox above ^."]
  [react/text {:style {:margin 20}}
   "And you can edit code, try to change text HERE"]
  [react/touchable-highlight {:on-press #(js/alert "HEY!")}
   [react/text {:style {:font-weight :bold}} "PRESS ME!"]]
  [react/text {:style {:margin 20}} "You can find component and screens examples in 'Components` and 'Screens` sections"]]]