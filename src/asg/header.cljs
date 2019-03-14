(ns asg.header
  (:require [cljs-styled-components.reagent :refer [defstyled]]
            [asg.styles.colors :as colors]
            [asg.styles.typography :as typo]
            [asg.styles.alignment :as align]))

(defstyled StyledHeader :header
  {:background colors/primary})

(defstyled HeaderContainer align/Container
  {:align-items     "center"
   :display         "flex"
   :flex-direction  "row"
   :justify-content "space-between"})

(defstyled ASG-Text :h1
  {:color           "white"
   :text-decoration "none"
   :letter-spacing  "0.05em"
   :margin          "0 0 0 0.1em"
   :text-shadow     (typo/text-stripper {:colors (map colors/stripes [:yellow :red])
                                         :step 0.05})
   :text-transform  "uppercase"})

(defn Asg []
  [typo/Link {:href "/"}
   [ASG-Text {} "ASG"]])

(defn Nav []
  [:div (for [event ["events" "recent" "about" "join"]]
          ^{:key event}
          [typo/Navlink {:href (str "#" event)} event])])

(defn Header []
  [StyledHeader
   [HeaderContainer
    [Asg]
    [Nav]]])
