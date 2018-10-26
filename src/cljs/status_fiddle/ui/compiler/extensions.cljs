(ns status-fiddle.ui.compiler.extensions
  (:require [status-im.ui.components.react :as react]
            [status-im.ui.components.colors :as colors]))

(defn extensions-code [content]
  (str
    "(def view react/view)
    (def text react/text) "

    "(defn message-container [outgoing]
      (let [properties {:outgoing outgoing}]
        [react/view
         [react/view {:style {:flex-direction (if outgoing :row-reverse :row)
                              :width          230
                              :align-self     (if outgoing :flex-end :flex-start)
                              :align-items    (if outgoing :flex-end :flex-start)}}
          [react/view {:style {:flex-direction :column
                               :width          230
                               :padding-left   8
                               :padding-right  8
                               :align-items    (if outgoing :flex-end :flex-start)}}
           [react/view {:style {:flex-direction (if outgoing :row-reverse :row)}}
            [react/view {:style {:margin-top         4
                                 :flex               1
                                 :padding-vertical   6
                                 :padding-horizontal 12
                                 :border-radius      8
                                 :padding-top        12
                                 :padding-bottom     10
                                 :background-color   (if outgoing colors/blue colors/white)}}

             " content "]]]]]))

      [react/view {:style {:flex 1 :background-color colors/gray-lighter}}
       [message-container false]
       [message-container true]]"))