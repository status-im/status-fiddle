(ns status-im.ui.screens.chat.photos
  (:require [status-im.ui.components.react :as react]))

(defn- source [photo-path]
  {:uri ""})

(defn photo [photo-path {:keys [size
                                accessibility-label]}]
  [react/view])

(defn member-photo [from]
  (photo nil {}))