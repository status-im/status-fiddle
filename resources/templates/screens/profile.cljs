(let [;; STYLES
      st-profile                              {:flex             1
                                               :background-color colors/white
                                               :flex-direction   :column}
      st-label-action-text                    {:padding-right 16
                                               :color         colors/blue
                                               :font-size     15}
      st-profile-form                         {:background-color colors/white
                                               :padding          16}

      st-profile-header-display               {:flex-direction  :column
                                               :justify-content :center
                                               :align-items     :center}
      st-profile-header-name-container        {:flex            1
                                               :justify-content :center}
      st-profile-name-text                    {:padding-vertical 14
                                               :font-size        15
                                               :text-align       :center}
      st-actions-list                         {:background-color styles/color-white}
      st-share-contact-code                   {:margin-horizontal 16
                                               :flex-direction    :row
                                               :justify-content   :space-between
                                               :align-items       :center
                                               :height            42
                                               :border-radius     8
                                               :background-color  (colors/alpha colors/blue 0.1)}

      st-share-contact-code-text-container    {:padding-left    16
                                               :padding-bottom  1
                                               :flex            0.9
                                               :flex-direction  :row
                                               :justify-content :center
                                               :align-items     :center}

      st-share-contact-code-text              {:color     colors/blue
                                               :font-size 15}
      st-share-contact-icon-container         {:border-radius   50
                                               :flex            0.1
                                               :padding-right   5
                                               :align-items     :center
                                               :justify-content :center}

      st-my-profile-info-container            {:background-color colors/white}

      st-advanced-button                      {:margin-top    16
                                               :margin-bottom 12}

      st-advanced-button-container            {:align-items     :center
                                               :justify-content :center}

      st-advanced-button-container-background {:padding-left     16
                                               :padding-right    12
                                               :padding-vertical 6
                                               :border-radius    18
                                               :background-color (colors/alpha colors/blue 0.1)}

      st-advanced-button-row                  {:flex-direction :row
                                               :align-items    :center}

      st-advanced-button-label                {:font-size      15
                                               :letter-spacing -0.2
                                               :color          colors/blue}
      st-settings-title                       {:color       colors/gray
                                               :margin-left 16
                                               :margin-top  18
                                               :font-size   14}
      st-settings-item-separator              {:margin-left 16}

      st-settings-item                        {:padding-horizontal 16
                                               :flex               1
                                               :flex-direction     :row
                                               :align-items        :center
                                               :background-color   colors/white
                                               :height             52}

      st-settings-item-text-wrapper           {:flex            1
                                               :flex-direction  :row
                                               :justify-content :space-between}

      st-settings-item-text                   {:flex-wrap :nowrap
                                               :font-size 15}
      st-settings-item-value                  {:flex          1
                                               :flex-wrap     :nowrap
                                               :text-align    :right
                                               :padding-right 10
                                               :font-size     15
                                               :color         colors/gray}

      ;; TEXT
      edit                                    "Edit"
      acc-name                                "Andrey"
      share-contact-code                      "Share contact code"
      wallet-advanced                         "Advanced"
      settings                                "Settings"
      notifications                           "Notifications"
      main-currency                           "Main Currency"

      settings-item                           (fn [label value]
                                                [react/touchable-highlight {:on-press #()}
                                                 [react/view st-settings-item
                                                  [react/view st-settings-item-text-wrapper
                                                   [react/text {:style           st-settings-item-text
                                                                :number-of-lines 1}
                                                    label]
                                                   (when value
                                                     [react/text {:style           st-settings-item-value
                                                                  :number-of-lines 1
                                                                  :uppercase?      true}
                                                      value])]
                                                  [icons/icon :icons/forward {:color colors/gray}]]])]

  ;; PROFILE SCREEN
  [react/view st-profile
   [toolbar/toolbar {}
    nil
    [toolbar/content-title ""]
    [react/touchable-highlight
     {:on-press #(js/alert edit)}
     [react/view
      [react/text {:style      st-label-action-text
                   :uppercase? true}
       edit]]]]
   [react/scroll-view
    [react/view st-profile-form
     [react/view st-profile-header-display
      [chat-icon.screen/my-profile-icon {:account {:name acc-name}
                                         :edit?   false}]
      [react/view st-profile-header-name-container
       [react/text {:style           st-profile-name-text
                    :number-of-lines 1}
        acc-name]]]]
    [react/view st-actions-list
     [react/touchable-highlight {:on-press #(js/alert share-contact-code)}
      [react/view st-share-contact-code
       [react/view st-share-contact-code-text-container
        [react/text {:style      st-share-contact-code-text
                     :uppercase? true}
         share-contact-code]]
       [react/view {:style st-share-contact-icon-container}
        [icons/icon :icons/qr {:color colors/blue}]]]]]
    [react/view st-my-profile-info-container
     [react/view
      [react/text {:style st-settings-title}
       settings]
      [settings-item main-currency "USD"]
      [components.common/separator st-settings-item-separator]
      [settings-item notifications]]]
    [react/view
     [react/touchable-highlight {:on-press #()
                                 :style    st-advanced-button}
      [react/view {:style st-advanced-button-container}
       [react/view {:style st-advanced-button-container-background}
        [react/view {:style st-advanced-button-row}
         [react/text {:style st-advanced-button-label}
          wallet-advanced]
         [icons/icon :icons/down {:color colors/blue}]]]]]]]])
