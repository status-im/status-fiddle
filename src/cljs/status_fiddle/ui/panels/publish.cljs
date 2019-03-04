(ns status-fiddle.ui.panels.publish
  (:require-macros [status-im.utils.views :refer [defview letsubs]])
  (:require [status-im.ui.components.react :as react]
            [re-frame.core :as re-frame]
            [status-im.ui.components.colors :as colors]
            [status-fiddle.ui.components.views :as components]
            [status-fiddle.utils :as utils]))

(defview publish []
  (letsubs [{:keys [in-progress? url]} [:get :publish]]
    (let [{:keys [extension? hash]} url]
      (println "url" extension? hash)
      [react/view {:style {:margin-vertical 20 :border-width 1 :border-color :gray :padding 20 :flex-direction :row}}
       (if in-progress?
         [react/activity-indicator {:animating true}]
         (if hash
           (if extension?
             (let [ext-url (str "https://get.status.im/extension/ipfs@" hash "/")]
               [react/view {:margin-horizontal 20}
                [react/text {:style {:margin-vertical 5 :font-weight :bold}} "Scan QR to install extension"]
                [react/text "Open Status -> Press (+) -> Scan QR "]
                [react/view {:align-items :center}
                 [react/view {:margin 20}
                  [react/qr-code {:value ext-url}]]]
                [react/text {:style {:margin-vertical 5 :font-weight :bold}} "OR share extension URL"]
                [react/view {:style {:flex-direction :row}}
                 [react/text ext-url]]])
                 ;[components/button "Copy" #() true]]])
             [react/text (str (utils/current-url))])
           [react/touchable-highlight {:on-press #(re-frame/dispatch [:publish-source-to-ipfs])}
            [react/view {:style {:background-color colors/blue
                                 :border-radius    4 :padding-horizontal 10 :padding-vertical 10
                                 :margin-bottom    5 :margin-left 10}}
             [react/text {:style {:color :white :font-size 11}}
              "PUBLISH"]]]))])))