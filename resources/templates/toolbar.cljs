[react/view {:style {:flex 1}}

 [toolbar/simple-toolbar "Simple toolbar"]

 [toolbar/toolbar {}
  toolbar/default-nav-back
  [toolbar/content-title "Simple toolbar"]]

 [toolbar/toolbar {} nil
  [toolbar/content-title "Only title"]]

 [toolbar/toolbar {} nil
  [toolbar/content-title "With action"]
  [toolbar/actions [{:icon      :icons/ok
                     :icon-opts {:color :blue}
                     :handler   #(js/alert "Toolbar OK handler")}]]]]