[react/view {:flex 1 :justify-content :space-around}

 [react/view {:align-items :center}
  [components.common/button {:on-press #(js/alert "Button")
                             :label    "Button"}]]

 [components.common/button {:on-press #(js/alert "Button")
                            :label    "Button"}]

 [components.common/button {:on-press    #(js/alert "Button without background")
                            :label       "Button without background"
                            :background? false}]]