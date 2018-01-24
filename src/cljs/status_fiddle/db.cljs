(ns status-fiddle.db)

(def default-db
  {:phone-name    "iPhone 6"
   :screen-width  375
   :screen-height 667
   :source        ""})

(def default-template
  "[react/view {:style {:flex             1\n                     :background-color \"#4360df\"}}\n [react/view {:style {:align-items :center}}\n  [react/text {:style      {:color     :white\n                            \n                            :font-size 17}}\n   \"Transaction Sent\"]\n  [icons/icon :icons/ok {:color styles/color-green-1}]\n  [react/text {:style {:color              :white\n                       :opacity            0.6\n                       :font-size          14\n                       :text-align         :center\n                       :padding-horizontal 16}}\n   \"Description\"]]\n [react/view {:style {:flex 1}}]\n [react/touchable-highlight {:on-press #(js/alert \"Got it!\")}\n  [react/view {:style {:align-items      :center\n                       :padding-vertical 18}}\n   [react/text {:style {:color     :white\n                        :font-size 15}}\n    \"Got it\"]]]]")
