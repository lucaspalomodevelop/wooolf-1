export const CharacterTypeEnum = {
  WOLF:       "WOLF",
  HUNTINGDOG: "HUNTINGDOG",
  HUNTER:     "HUNTER",
  SHEPHERD:   "SHEPHERD",
  SHEEP:      "SHEEP",
} as const;

export type CharacterTypeEnum = typeof CharacterTypeEnum[keyof typeof CharacterTypeEnum];