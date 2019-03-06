(ns asg.sections.events
  (:require [reagent.core :as r]
            [asg.styles.alignment :as align]
            [asg.styles.typography :as typo]))

(def events-url "https://wnephqc0h5.execute-api.us-east-1.amazonaws.com/prod/events/upcoming")

(comment
  ;; nice that the name is now mostly just "ASG Meeting in
  ;; April". There's no title information.
  {:id        "04g5ofo2p67nvlu1sudqtvlpji",
   :name      "Acadiana Software Group March 2019 Meeting",
   :location  "Lafayette Science Museum, 433 Jefferson St, Lafayette, LA 70501, USA",
   :url       "https://www.google.com/calendar/event?eid=MDRnNW9mbzJwNjdudmx1MXN1ZHF0dmxwamkgbW1xb3ZkbmgycXViZjZsdDBvM3VmMG12dmNAZw",
   :startDate "2019-03-13T23:30:00.000Z",
   :endDate   "2019-03-14T01:00:00.000Z",
   :source    "Acadiana Software Group Calendar"})

(defn EventList [events]
  [:ul
   (doall
    (for [{title :name :keys [id] :as event} events]
      ^{:key (:id event)}
      [:li title]))])

(defn UpcomingEvents [{:keys [max-events]}]
  (let [state          (r/atom {:stage  :loading
                                :events []})
        now            (js/Date.)
        in-the-future? (fn [{:keys [startDate]}] (> (js/Date. startDate) now))]
    (r/create-class
     {:component-did-mount
      (fn []
        (-> (js/fetch events-url)
            (.then #(.json %))
            (.then #(js->clj % :keywordize-keys true))
            (.then #(:events %))
            (.then (fn [events]
                     (reset! state {:state  :loaded
                                    :events (->> events
                                                 (filter in-the-future?)
                                                 (sort-by :startDate)
                                                 (take max-events))})))
            (.catch (fn [_e]
                      (reset! state {:state :error})))))
      :render
      (fn [{:keys [max-events]}]
        (let [{:keys [stage events]} @state]
          (cond (= stage :loading)
                [:p "Loading events..."]

                (= stage :error)
                [:p "Error loading events..."]

                (-> events count zero?)
                [:p "No upcoming events"]

                (-> events count pos?)
                [EventList events]

                :else
                [:p "Unknown issue with events"])))})))

(defn Events [{:keys [max-events]}]
  [align/Container
   [typo/section-header "Events"]
   [UpcomingEvents {:max-events max-events}]])
