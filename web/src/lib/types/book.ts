import type { Empty } from './common';
// Book-related DTOs aligned with backend

export type BookSummary = {
  bookId: number;
  name: string;
  publishedAt: string; // ISO date YYYY-MM-DD
  canRent: boolean;
};

export type ListBookSummaryResponse = {
  books: BookSummary[];
};

export type BookDetailResponse = {
  bookId: number;
  name: string;
  outline: string;
  publishedAt: string; // ISO date YYYY-MM-DD
  author: string;
  depositedAt: string; // ISO datetime with offset
  isbn: string;
  canRent: boolean;
};

export type CreateBookRequest = {
  title: string;
  author: string;
  outline: string;
  publishedAt: string; // ISO date YYYY-MM-DD
  isbn: string;
};

export type UpdateBookRequest = {
  title: string;
  author: string;
  outline: string;
  publishedAt: string; // ISO date YYYY-MM-DD
};

export type DeleteBookResponse = Empty;
export type CreateBookResponse = {
  bookId: number;
  name: string;
  outline: string;
  publishedAt: string; // ISO date YYYY-MM-DD
  author: string;
  depositedAt: string; // ISO datetime with offset
  isbn: string;
};
export type UpdateBookResponse = Empty;

// Rental responses
export type RentBookResponse = {
  rentId: string;
  bookId: number;
  userId: string;
  rentalAt: string; // ISO datetime with offset
  returnDeadline: string; // ISO datetime with offset
  status: string;
};

export type ReturnBookResponse = {
  rentId: string;
  bookId: number;
  userId: string;
  rentalAt: string; // ISO datetime with offset
  returnDeadline: string; // ISO datetime with offset
  returnedAt: string | null;
  status: string;
};
