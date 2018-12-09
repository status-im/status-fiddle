(ns status-fiddle.ui.db
  (:require [cemerick.url :as url]))

(defn current-url []
  (url/url (-> js/window .-location .-href)))

(def default-db
  {:phone-name    "iPhone 6"
   :screen-width  375
   :screen-height 667
   :source        {}
   :cm            {}
   :os            "ios"
   :forms         {:colors     false
                   :icons      false
                   :svg        false
                   :css        false
                   :help       false
                   :components false
                   :screens    false
                   :extensions (boolean (-> (current-url) :anchor url/query->map (get "ext")))}})