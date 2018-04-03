(ns status-fiddle.core
  (:require app.compile
            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            status-fiddle.ui.events
            status-fiddle.ui.subs
            [status-fiddle.ui.views :as views]))

(set! js/window.onerror
      #(re-frame/dispatch [:set :error %1]))

(enable-console-print!)

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (re-frame/dispatch [:load-code])
  (mount-root))