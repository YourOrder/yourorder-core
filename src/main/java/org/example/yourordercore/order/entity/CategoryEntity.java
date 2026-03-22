package org.example.yourordercore.order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "category_entity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CategoryEntity parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CategoryEntity> subcategories = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    @Builder.Default
    private List<ProductEntity> products = new ArrayList<>();

    public void addSubcategory(CategoryEntity subcategory) {
        if (subcategory == null) {
            throw new IllegalArgumentException("Subcategory cannot be null");
        }
        if (!this.subcategories.contains(subcategory)) {
            subcategory.setParent(this);
            this.subcategories.add(subcategory);
        }
    }

    public void removeSubcategory(CategoryEntity subcategory) {
        if (subcategory == null) {
            throw new IllegalArgumentException("Subcategory cannot be null");
        }
        this.subcategories.remove(subcategory);
        subcategory.setParent(null);
    }

    public void addProduct(ProductEntity product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (!this.products.contains(product)) {
            product.setCategory(this);
            this.products.add(product);
        }
    }

    public void removeProduct(ProductEntity product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        this.products.remove(product);
        product.setCategory(null);
    }
}
