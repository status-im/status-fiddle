(let [;; STYLES
      st-intro-view              {:flex               1
                                  :padding-horizontal 30
                                  :background-color   colors/white}
      st-intro-logo-container    {:flex            1
                                  :align-items     :center
                                  :justify-content :center}
      st-intro-text              {:text-align     :center
                                  :color          colors/black
                                  :line-height    28
                                  :font-size      22
                                  :font-weight    :bold
                                  :letter-spacing -0.3}
      st-intro-text-description  {:line-height    21
                                  :margin-top     8
                                  :margin-bottom  16
                                  :font-size      14
                                  :letter-spacing -0.2
                                  :text-align     :center
                                  :color          colors/gray}
      st-buttons-container       {:align-items :center}
      st-bottom-button-container {:margin-bottom 6
                                  :margin-top    38}
      st-intro-logo              {:size      111
                                  :icon-size 46}

      ;;TEXT
      intro-text "Status is an open source decentralized chat and Ethereum browser"
      intro-text-description "Status is built with the help of the community to help you use all the benefits of decentralized web in your mobile phone"
      create-label "Create account"
      already-have-label "Already have account"]



  ;; INTRO SCREEN
  [react/view {:style st-intro-view}
   [react/view {:style st-intro-logo-container}
    [components.common/logo st-intro-logo]]
   [react/text {:style st-intro-text}
    intro-text]
   [react/view
    [react/text {:style st-intro-text-description}
     intro-text-description]]
   [react/view st-buttons-container
    [components.common/button {:button-style {:flex-direction :row}
                               :on-press     #(js/alert create-label)
                               :label create-label}]
    [react/view st-bottom-button-container
     [components.common/button {:on-press    #(js/alert already-have-label)
                                :label       already-have-label
                                :background? false}]]]])