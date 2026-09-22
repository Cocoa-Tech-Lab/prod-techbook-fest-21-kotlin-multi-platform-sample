// Types for account-related API responses
export type RentalHistoryItem = {
  rentId: string
  bookId: number
  userId: string
  status: string
  rentalAt: string // ISO datetime with offset
  returnDeadline: string // ISO datetime with offset
  returnedAt: string | null
}

export type RentalListResponse = {
  items: RentalHistoryItem[]
}
