(ns status-fiddle.db)

(def default-db
  {:phone-name    "iPhone 6"
   :screen-width  375
   :screen-height 667
   :source        ""
   :os            "ios"
   :forms         {:colors false
                   :icons  false
                   :svg    false
                   :css    false
                   :help   false}})

(def default-template
  "[react/view {:style {:flex 1}}\n [toolbar/toolbar {} \n  toolbar/default-nav-back \n  [toolbar/content-title \"Status\"]]\n [react/view {:style {:flex 1\n                      :align-items :center\n                      :justify-content :center}}\n  [icons/icon :icons/profile-active]  \n  [react/text {:style {:font-size 25 :padding 20} \n               :uppercase? platform/android?} \n   \"Hello!\"]\n  [react/text {:style {:padding-horizontal 20}}\n   \"This is an \" (if platform/ios? \"iPhone\" \"Android Phone\") \", but you can change device.\"]\n  [react/text {:style {:margin 20}}\n    \"And you can edit code, try to change text HERE\"]]]")