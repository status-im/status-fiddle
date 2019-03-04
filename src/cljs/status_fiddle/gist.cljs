(ns status-fiddle.gist
  (:require [ajax.core :as ajax]))

(defn load [id handler]
  "Finds a gist by id, swaps it into atom's :text key,
   and calls callback when it's given."
  (ajax/GET (str "https://api.github.com/gists/" id)
            {:with-credentials? false
             :handler           (fn [x]
                                  (let [response-text
                                        (->> (get x "files")
                                             vals
                                             (keep (fn [x] (get x "content")))
                                             first)]
                                    (handler response-text)))}))