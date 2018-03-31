(ns status-fiddle.templates
  (:require-macros [status-im.utils.slurp :refer [slurp]]))

(def default (slurp "resources/templates/default.cljs"))
(def button (slurp "resources/templates/button.cljs"))
(def button2 (slurp "resources/templates/button2.cljs"))
(def input (slurp "resources/templates/input.cljs"))
(def toolbar (slurp "resources/templates/toolbar.cljs"))
(def action-button (slurp "resources/templates/action_button.cljs"))
(def bottom-buttons (slurp "resources/templates/bottom_buttons.cljs"))
(def bottom-buttons2 (slurp "resources/templates/bottom_buttons2.cljs"))
(def contact (slurp "resources/templates/contact.cljs"))