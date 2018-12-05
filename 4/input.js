regExps = {
"exercise_1": /[A-Z][a-z]+/,
"exercise_2": /088[1-9]\d{6}/,
"exercise_3": /[^10]+/,
"exercise_4": /^[a-zA-Z][a-zA-Z._\d]{2,30}$/,
"exercise_5": /1[0-4]\d{2}|1500|^\d{3}$/,
"exercise_6": /class=['"][\w ]*['"]/
};
cssSelectors = {
"exercise_1": "css > item > java",
"exercise_2": "css > different > different > java",
"exercise_3": "css > item > java > tag",
"exercise_4": "css #someId + item",
"exercise_5": "item > tag > java + java.class1",
"exercise_6": "css > item#someId item > item > item > item",
"exercise_7": "css > different > different#diffId2 > java + java",
"exercise_8": "#someId"
};
