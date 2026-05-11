export class CharacterType {
	private readonly trueIdentity: CharacterType;
	private readonly appearance: CharacterType;
	private readonly rankValue: number;
	private readonly targets: ReadonlyArray<CharacterType>;

	constructor(
		trueIdentity: CharacterType,
		appearance: CharacterType,
		rankValue: number,
		targets: CharacterType[],
	) {
		this.trueIdentity = trueIdentity;
		this.appearance = appearance;
		this.rankValue = rankValue;
		this.targets = [...targets];
	}

	getTrueIdentity(): CharacterType {
		return this.trueIdentity;
	}

	getAppearance(): CharacterType {
		return this.appearance;
	}

	getRankValue(): number {
		return this.rankValue;
	}

	getTargets(): ReadonlyArray<CharacterType> {
		return this.targets;
	}

	getCharacterType(): CharacterType {
		return this.appearance;
	}

	toString(): string {
		return `character{trueIdentity=${this.trueIdentity}, appearance=${this.appearance}, rankValue=${this.rankValue}, targets=${this.targets.join(", ")}}`;
	}
}
