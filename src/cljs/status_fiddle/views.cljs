(ns status-fiddle.views
  (:require [re-frame.core :as re-frame]
            [status-fiddle.subs :as subs]
            [status-fiddle.react-native-web :as react]))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [react/view
     [:meta {:http-equiv "Content-Type" :content "text/html; charset=UTF-8"}]
     [:link {:rel "stylesheet" :type "text/css" :href "https://storage.googleapis.com/app.klipse.tech/css/codemirror.css"}]
     [:link {:rel "stylesheet" :type "text/css" :href "css/theme.css"}]
     [:pre [:code {:class "language-klipse"} "(map inc [1 2 3])"]]]))
