(ns status-fiddle.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            status-fiddle.ui.events
            status-fiddle.ui.subs
            [status-fiddle.ui.views :as views]
            [status-fiddle.config :as config]))

(set! js/window.onerror
      #(re-frame/dispatch [:set :error %1]))

(defn dev-setup []
  (if config/debug?
    (enable-console-print!)
    (set! js/COMPILED true)))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (re-frame/dispatch [:load-code])
  (dev-setup)
  (mount-root))