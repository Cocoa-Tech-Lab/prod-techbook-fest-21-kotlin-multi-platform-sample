import { http } from '../http';
import type {
  BookDetailResponse,
  ListBookSummaryResponse,
  CreateBookRequest,
  UpdateBookRequest,
  CreateBookResponse,
  UpdateBookResponse,
  DeleteBookResponse,
  RentBookResponse,
} from '../types/book';

export const booksApi = {
  list: () => http.get<ListBookSummaryResponse>('/books'),
  detail: (bookId: number) => http.get<BookDetailResponse>(`/books/${bookId}`),
  create: (body: CreateBookRequest) => http.post<CreateBookResponse, CreateBookRequest>('/books', body),
  update: (bookId: number, body: UpdateBookRequest) =>
    http.put<UpdateBookResponse, UpdateBookRequest>(`/books/${bookId}`, body),
  delete: (bookId: number) => http.delete<DeleteBookResponse>(`/books/${bookId}`),
  rent: (bookId: number) => http.post<RentBookResponse, void>(`/books/${bookId}/rent`),
  returnRental: (rentId: string) => http.post<import('../types/book').ReturnBookResponse, void>(`/rental/${rentId}/return`),
};
