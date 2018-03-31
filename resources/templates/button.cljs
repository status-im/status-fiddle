[react/view {:flex 1}
 [react/view {:flex 1 :background-color colors/blue :justify-content :center :margin-vertical 20}

  [button/button {:disabled? false
                  :on-press  #(js/alert "Button pressed")}
   "Button enabled"]

  [button/button {:disabled?  false
                  :text-style {:color :black}
                  :on-press   #(js/alert "Button pressed")}
   "Button black"]

  [button/button {:disabled? true
                  :on-press  #(js/alert "Button pressed")}
   "Button disabled  3333"]]

 [button/primary-button nil "Primary button"]

 [react/view {:height 20}]

 [button/secondary-button nil "Secondary button"]]