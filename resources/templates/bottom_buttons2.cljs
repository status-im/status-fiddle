[react/view {:flex 1}
 [react/view {:flex 1}]

 [react/view {:flex-direction    :row
              :margin-horizontal 12
              :margin-vertical   15
              :align-items       :center}

  [components.common/bottom-button
   {:label    "Back"
    :on-press #()}]

  [react/view {:style {:flex 1}}]

  [components.common/bottom-button
   {:forward? true
    :label    "Next"
    :on-press #()}]]]