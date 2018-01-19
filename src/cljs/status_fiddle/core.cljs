(ns status-fiddle.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [status-fiddle.events :as events]
            [status-fiddle.views :as views]
            [status-fiddle.config :as config]))

(set! js/COMPILED true)

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
