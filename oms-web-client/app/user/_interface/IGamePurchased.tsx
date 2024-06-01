export interface IGamePurchased {
  id: number;
  name: string;
  description: string;
  releaseDate: string;
  price: number;
  genres: {
    id: number;
    name: string;
  }[];
}
