(ns status-fiddle.db)

(def default-db
  {:phone-name    "iPhone 6"
   :screen-width  375
   :screen-height 667
   :source        ""
   :os            "ios"})

(def default-template
  "[react/view {:style {:flex 1\n                     :background-color styles/color-blue2\n                     :align-items :center\n                     :justify-content :center}}\n [icons/icon :icons/profile-active {:color :white}]  \n [react/text {:style {:color :white :font-size 25 :padding 20}} \"Hello!\"]\n [react/text {:style {:color styles/color-white-transparent-5 :padding-horizontal 20}}\n  \"This is an \" (if platform/ios? \"iPhone\" \"Android Phone\") \", but you can change device.\"]\n [react/text {:style {:color styles/color-white-transparent-5\n                      :margin 20}}\n   \"And you can edit code, try to change text HERE\"]]")
