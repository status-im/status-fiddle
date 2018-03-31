[bottom-buttons/bottom-buttons
 {:background-color colors/blue
  :padding-vertical 8}

 [button/button {:style    {:flex 1}
                 :on-press #()}
  "Cancel"]

 [button/button {:style    {:flex-direction :row
                            :align-items    :center}
                 :on-press #()}
  "Sign"
  [icons/icon :icons/forward {:color :white}]]]