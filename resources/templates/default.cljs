[react/view {:style {:flex 1}}
 [toolbar/toolbar {}
  toolbar/default-nav-back
  [toolbar/content-title "Status"]]
 [react/view {:style {:flex 1
                      :align-items :center
                      :justify-content :center}}
  [icons/icon :icons/profile-active]
  [react/text {:style {:font-size 25 :padding 20}
               :uppercase? platform/android?}
   "Hello!"]
  [react/text {:style {:padding-horizontal 20}}
   "This is an " (if platform/ios? "iPhone" "Android Phone") ", but you can change device."]
  [react/text {:style {:margin 20}}
   "And you can edit code, try to change text HERE HAHA"]]]