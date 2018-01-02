(ns status-fiddle.views
  (:require [re-frame.core :as re-frame]
            [status-fiddle.subs :as subs]
            [status-fiddle.react-native-web :as react]))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [react/view
     [react/text "Hello from "]]))
